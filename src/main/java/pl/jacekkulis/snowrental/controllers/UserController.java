package pl.jacekkulis.snowrental.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.jacekkulis.snowrental.models.User;
import pl.jacekkulis.snowrental.services.IUserService;

@Controller
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private IUserService userService;

	@Autowired
	ApplicationEventPublisher eventPublisher;
	
	@RequestMapping(value ="/user/edit/email", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseBody 
	public String editUserEmail(@RequestParam int id, @RequestParam String value) {
		logger.info("editUserEmaiL");
		User user = userService.findById(id);
		user.setEmail(value);
		userService.update(user);
		return "userController: success";
	}
	
	
	@RequestMapping(value ="/user/edit/firstName", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseBody 
	public String editUserFirstName(@RequestParam int id, @RequestParam String value) {
		User user = userService.findById(id);
		user.setFirstName(value);
		userService.update(user);
		return "userController: success";
	}
	
	@RequestMapping(value ="/user/edit/lastName", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseBody 
	public String editUserLastName(@RequestParam int id, @RequestParam String value) {
		User user = userService.findById(id);
		user.setLastName(value);
		userService.update(user);
		return "userController: success";
	}
	
	@RequestMapping(value ="/user/edit/enabled", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_USER')")
	@ResponseBody 
	public String editUserEnabled(@RequestParam int id, @RequestParam String value) {
		User user = userService.findById(id);
		user.setEnabled(Boolean.valueOf(value));
		userService.update(user);
		return "userController: success";
	}
}