package pl.jacekkulis.snowrental.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import pl.jacekkulis.snowrental.models.Message;
import pl.jacekkulis.snowrental.models.User;
import pl.jacekkulis.snowrental.services.IMessageService;
import pl.jacekkulis.snowrental.services.IUserService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private IUserService userService;

	@Autowired
	private IMessageService messageService;

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	// for 403 access denied page
	@RequestMapping(value = "/403", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView accesssDenied(Principal user) {
		ModelAndView model = new ModelAndView();

		if (user != null) {
			model.addObject("msg", "Hi " + user.getName() + ", you do not have permission to access this page!");
			logger.error("Hi " + user.getName() + ", you do not have permission to access this page!");
		} else {
			logger.error("You do not have permission to access this page!");
			model.addObject("msg", "You do not have permission to access this page!");
		}

		model.setViewName("error/accessDenied");
		return model;
	}

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView home(ModelAndView modelAndView, Principal principal, HttpServletRequest request,
			@ModelAttribute("message") String message, @ModelAttribute("type") String type, HttpSession session) {
		Locale locale = localeResolver.resolveLocale(request);
		logger.info("Welcome home! The client current locale is {}.", locale);

		if (message != null && type != null) {
			modelAndView.addObject("message", message);
			modelAndView.addObject("type", type);
		}

		if (principal != null) {
			logger.info("homeController: user is authenticated: " + principal.getName());

			User actualUser = userService.findByEmail(principal.getName());

			List<Message> messageList = new ArrayList<Message>();
			messageList.addAll(actualUser.getMessages());

			if (messageList != null) {
				logger.info("message list not null");
				for (Message msg : messageList) {
					if (!msg.isRead()) {
						logger.info("message: " + msg);
						modelAndView.addObject("userMessage", msg.getContent());
						modelAndView.addObject("userMessageType", msg.getMessageType());
						modelAndView.addObject("userMessageId", msg.getId());
					}
				}
			}
			
			session.setAttribute("user", actualUser);
		} else {
			logger.info("homeController: user is not authenticated");
		}

		modelAndView.setViewName("home/index");
		return modelAndView;
	}

	@RequestMapping(value = "/userReadMessage", method = RequestMethod.GET)
	public String userReadmessage(@RequestParam String id, HttpServletRequest request) {
		logger.info("userReadmessage: messageId: " + id);
		Message message = messageService.findById(Integer.valueOf(id));
		message.setRead(true);
		messageService.update(message);
		return "redirect:/";
	}
}
