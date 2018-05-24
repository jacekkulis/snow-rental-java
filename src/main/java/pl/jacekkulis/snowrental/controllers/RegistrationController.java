package pl.jacekkulis.snowrental.controllers;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.jacekkulis.snowrental.captcha.ICaptchaService;
import pl.jacekkulis.snowrental.models.MessageType;
import pl.jacekkulis.snowrental.models.User;
import pl.jacekkulis.snowrental.models.VerificationToken;
import pl.jacekkulis.snowrental.registration.OnRegistrationCompleteEvent;
import pl.jacekkulis.snowrental.services.IUserService;

@Controller
public class RegistrationController {

	private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private IUserService iUserService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ICaptchaService captchaService;

	@Autowired
	ApplicationEventPublisher eventPublisher;

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView showRegistrationPage(ModelAndView modelAndView, User user) {
		logger.info("showRegistrationPage");
		modelAndView.addObject("user", user);
		modelAndView.setViewName("user/register");
		return modelAndView;
	}

	// Process form input data
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView processRegistrationForm(ModelAndView modelAndView, @ModelAttribute("user") @Valid User user,
			BindingResult bindingResult, HttpServletRequest request, RedirectAttributes attributes) {
		logger.info("processRegistrationForm start");

		Locale locale = localeResolver.resolveLocale(request);
		// Lookup user in database by e-mail
		User userExists = iUserService.findByEmail(user.getEmail());

		if (userExists != null) {
			logger.info("User already exists: " + userExists);
			bindingResult.rejectValue("email", "messageCode",
					messageSource.getMessage("error.message.emailExists", null, locale));
		}

		if (!user.getRepeatPassword().equals(user.getPassword())) {
			logger.info("Passwords doesnt match");
			bindingResult.rejectValue("password", "messageCode",
					messageSource.getMessage("error.user.password.notmatch", null, locale));
			bindingResult.rejectValue("repeatPassword", "messageCode",
					messageSource.getMessage("error.user.password.notmatch", null, locale));
		}

		String reCaptchaResponse = request.getParameter("g-recaptcha-response");

		boolean isValid = false;

		try {
			isValid = captchaService.validate(reCaptchaResponse);
		} catch (Exception e) {
			logger.info("recaptchaError");
			attributes.addAttribute("message", messageSource.getMessage("error.message.recaptcha", null, locale));
			attributes.addAttribute("type", MessageType.danger);
		}

		if (!isValid) {
			logger.info("recaptcha is not valid " + isValid);
			modelAndView.addObject("message", messageSource.getMessage("error.user.reCaptchaResponse", null, locale));
			modelAndView.addObject("type", MessageType.danger);
			modelAndView.setViewName("user/register");
			return modelAndView;
		} else {
			logger.info("recaptcha is valid " + isValid);
		}

		if (bindingResult.hasErrors()) {
			logger.warn("Form has errors");
			for (Object obj : bindingResult.getAllErrors()) {
				if (obj instanceof FieldError) {
					FieldError fieldError = (FieldError) obj;
					String errorMessage = messageSource.getMessage(fieldError, null);
					logger.warn(errorMessage);
				}
			}
		} else {
			String appUrl = request.getContextPath();
			iUserService.create(user);
			eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, locale, appUrl));
			modelAndView.addObject("message", messageSource.getMessage("success.message.regSucc", null, locale));
			modelAndView.addObject("type", MessageType.success);
		}

		logger.info("processRegistrationForm end");
		modelAndView.setViewName("user/register");
		return modelAndView;
	}

	@RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
	public String confirmRegistration(HttpServletRequest request, Model model, @RequestParam("token") String token,
			RedirectAttributes redirectAttributes) {
		logger.info("confirmRegistration");

		Locale locale = localeResolver.resolveLocale(request);
		VerificationToken verificationToken = iUserService.getVerificationToken(token);

		if (verificationToken == null) {
			logger.info("confirmRegistration: invalid");
			return "redirect:/confirmationProblem";
		}

		User user = verificationToken.getUser();
		logger.info(user.toString());
		logger.info("confirmRegistration: everything good");

		user.setEnabled(true);
		iUserService.saveRegisteredUser(user);

		redirectAttributes.addFlashAttribute("message", messageSource.getMessage("success.message.confirmation", null, locale));
		redirectAttributes.addFlashAttribute("type", MessageType.success);
		return "redirect:/accountActivated";
	}

	@RequestMapping(value = "/confirmationProblem", method = RequestMethod.GET)
	public ModelAndView confirmationProblem(ModelAndView modelAndView, HttpServletRequest request) {
		logger.info("confirmationProblem");
		Locale locale = localeResolver.resolveLocale(request);
		modelAndView.addObject("message",  messageSource.getMessage("error.message.invalidToken", null, locale));
		modelAndView.addObject("type", MessageType.danger);
		modelAndView.setViewName("error/confirmationProblem");
		return modelAndView;
	}

	@RequestMapping(value = "/accountActivated", method = RequestMethod.GET)
	public ModelAndView accountActivatedPage(ModelAndView modelAndView, HttpServletRequest request) {
		logger.info("accountActivatedPage");
		Locale locale = localeResolver.resolveLocale(request);
		modelAndView.addObject("message",  messageSource.getMessage("label.accountActivated", null, locale));
		modelAndView.addObject("type", MessageType.success);
		modelAndView.setViewName("user/accountActivated");
		return modelAndView;
	}

}