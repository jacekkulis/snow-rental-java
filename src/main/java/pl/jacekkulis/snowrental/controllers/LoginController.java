package pl.jacekkulis.snowrental.controllers;

import java.security.Principal;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import pl.jacekkulis.snowrental.models.MessageType;

@Controller
public class LoginController {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private LocaleResolver localeResolver;
	
	@Autowired
	ApplicationEventPublisher eventPublisher;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public ModelAndView signInProcess(@RequestParam(value = "logout", required = false) String logout, Principal principal) {
		logger.info("signInProcess");
		ModelAndView mav = new ModelAndView();
		Locale locale = localeResolver.resolveLocale(request);
		
		if(principal != null) {
			mav.addObject("message", messageSource.getMessage("error.message.alreadyLogged", null, locale));
			mav.addObject("type", MessageType.info);
			mav.setViewName("redirect:/");
			return mav;
		}
		
		if (logout != null) {
			logger.info("signInProcess: logout param: " + logout);
			mav.addObject("message",  messageSource.getMessage("success.message.logoutSucc", null, locale));
			mav.addObject("type", MessageType.success);
		}
		
		mav.setViewName("user/signin");
		return mav;
	}
}