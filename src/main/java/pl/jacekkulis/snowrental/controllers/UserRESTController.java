package pl.jacekkulis.snowrental.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.jacekkulis.snowrental.models.User;
import pl.jacekkulis.snowrental.services.IUserService;

@RestController
public class UserRESTController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private IUserService userService;

	
	@RequestMapping(value ="/userRest/{userId}", method = RequestMethod.GET, produces = "application/json")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public User getUserJson(@PathVariable int userId) {
		logger.info("getUser json");
		User user = userService.findById(userId);
		return user;
	}
	
	@RequestMapping(value ="/userRest/{userId}.xml", method = RequestMethod.GET, produces = "application/xml")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public User getUserXml(@PathVariable int userId) {
		logger.info("getUser xml");
		User user = userService.findById(userId);
		return user;
	}
}