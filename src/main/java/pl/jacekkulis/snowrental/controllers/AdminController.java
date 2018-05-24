package pl.jacekkulis.snowrental.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import pl.jacekkulis.snowrental.models.Message;
import pl.jacekkulis.snowrental.models.MessageType;
import pl.jacekkulis.snowrental.models.Order;
import pl.jacekkulis.snowrental.models.OrderStatusCode;
import pl.jacekkulis.snowrental.models.User;
import pl.jacekkulis.snowrental.services.IMessageService;
import pl.jacekkulis.snowrental.services.IOrderService;
import pl.jacekkulis.snowrental.services.IUserService;
import java.security.Principal;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AdminController {

	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Autowired
	private IUserService userService;
	
	@Autowired
	private IOrderService orderService;
	
	@Autowired
	private IMessageService messageService;
	
	@Autowired
	private LocaleResolver localeResolver;

	@RequestMapping("/userList")
	@PreAuthorize("hasRole('ROLE_SERVICEMAN')")
	public String listPersons(Model model, HttpServletRequest request) {
		logger.info("listPersons");
		int userId = ServletRequestUtils.getIntParameter(request, "userId", -1);

		if (userId > 0)
			model.addAttribute("user", userService.findById(userId));
		else
			model.addAttribute("user", new User());

		model.addAttribute("userList", userService.findAll());
		return "user/userList";
	}
	
	@RequestMapping("/user/delete")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String deleteUser(@RequestParam int userId, Principal principal) {
		logger.info("delete");
		
		User user = userService.findById(userId);
		
		if (user != null && !principal.getName().equals(user.getEmail())) {
			logger.info("delete: " + user);
			userService.delete(userId);
		}
		else if (user != null && principal.getName().equals(user.getEmail())){
			logger.info("you cant delete yourself");
		}
		else {
			logger.info("user null");
		}
		
		return "redirect:/userList";
	}
	
	@RequestMapping("/user/sendMessage/{userId}")
	@PreAuthorize("hasRole('ROLE_SERVICEMAN')")
	public String sendMessage(@PathVariable("userId") int userId) {
		return "redirect:/userList";
	}
	
	@RequestMapping(value = "/userOrders", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_SERVICEMAN')")
	public ModelAndView listUserOrders(HttpServletRequest request, @RequestParam(value = "id", required = true) String id) {
		logger.info("listOrders");
		ModelAndView mav = new ModelAndView("admin/userOrderHistory");
		
		User foundUser = null;
	
		if (id != null) {
			foundUser = userService.findById(Integer.valueOf(id));
		}
		
		if (foundUser != null) {
			mav.addObject("orderList", orderService.findOrdersByUser(foundUser));
			mav.addObject("user", foundUser);
		}

		return mav;
	}
	
	
	@RequestMapping("/order/delete")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String deleteOrder( @RequestParam(value = "orderId", required = true) int orderId,  @RequestParam(value = "userId", required = true) int userId) {
		Order order = orderService.delete(orderId);
		User user = userService.findById(userId);
		
		if (user != null) {
			String content = "Your order " + order.getOrderName() + " has been deleted by administrator.";
			Message message = new Message(user, content, MessageType.danger);
			messageService.create(message);
		}
		
		return "redirect:/userOrders?id=" + userId;
	}
	
	@RequestMapping(value = "/order/changeStatusCode", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_SERVICEMAN')")
	@ResponseBody
	public String changeStatusCode( @RequestParam(value = "orderId", required = true) String orderId,  @RequestParam(value = "statusCode", required = true) String statusCode) {
		logger.info("updating orderId: " + orderId + ", statusCode: " + OrderStatusCode.valueOf(statusCode));
		
		Order order = orderService.findById(Integer.valueOf(orderId));
		
		if (order != null) {
			orderService.updateStatusCode(Integer.valueOf(orderId), OrderStatusCode.valueOf(statusCode));
			
			User user = userService.findById(order.getUser().getId());
			
			if (user != null) {
				String content = "Order " + order.getOrderName() + " changed status.";
				Message message = new Message(user, content, MessageType.warning);
				messageService.create(message);
			}
		}
		return "true";
	}
	
	@RequestMapping(value = "/changeLang")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String changeLang(@RequestParam(value = "lang", required = true) String lang, HttpServletRequest request, HttpServletResponse response) {
		if(lang.equals("en")) {
			localeResolver.setLocale(request, response, Locale.ENGLISH);
		} else if (lang.equals("pl")) {
			localeResolver.setLocale(request, response, new Locale("pl", "PL"));
		} else if (lang.equals("es")) {
			localeResolver.setLocale(request, response, new Locale("es", "ES"));
		}

		return "redirect:/";
	}
}
