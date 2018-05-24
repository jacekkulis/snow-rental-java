package pl.jacekkulis.snowrental.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.jacekkulis.snowrental.models.Basket;
import pl.jacekkulis.snowrental.models.BasketItem;
import pl.jacekkulis.snowrental.models.Item;
import pl.jacekkulis.snowrental.models.MessageType;
import pl.jacekkulis.snowrental.models.Order;
import pl.jacekkulis.snowrental.models.OrderDetails;
import pl.jacekkulis.snowrental.models.OrderStatusCode;
import pl.jacekkulis.snowrental.models.Ski;
import pl.jacekkulis.snowrental.models.Snowboard;
import pl.jacekkulis.snowrental.services.IItemService;
import pl.jacekkulis.snowrental.services.IOrderService;
import pl.jacekkulis.snowrental.services.IUserService;
import java.security.Principal;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class BasketController {

	private static final Logger logger = LoggerFactory.getLogger(BasketController.class);

	@Autowired
	private IItemService itemService;

	@Autowired
	private IUserService userService;
	
	@Autowired
	private IOrderService orderService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private LocaleResolver localeResolver;

	@RequestMapping("/basket")
	public ModelAndView showBasket(HttpSession session) {
		logger.info("showBasket");
		ModelAndView mav = new ModelAndView("item/basket");

		Basket basket = (Basket) session.getAttribute("basket");

		if (basket != null) {
			logger.info("basket not null");
			mav.addObject("basketItemList", basket.getItemsInBasket());
		} else {
			logger.info("showBasket");
			mav.addObject("message", "Basket is empty");
			mav.addObject("type", MessageType.info);
		}

		return mav;
	}

	@RequestMapping("/basket/add")
	@PreAuthorize("hasRole('ROLE_USER')")
	public String addToBasket(@RequestParam int id, HttpSession session) {
		logger.info("addToBasket");
		
		Item item = itemService.findById(id);
		BasketItem basketItem = new BasketItem();
		Basket basket = (Basket) session.getAttribute("basket");
		
		if (basket == null) {
			basket = new Basket();
		}

		if (item != null) {
			if (item instanceof Ski) {
				Ski ski = (Ski) item;
				basketItem.setItem(ski);
			} else if (item instanceof Snowboard) {
				Snowboard snowboard = (Snowboard) item;
				basketItem.setItem(snowboard);
			}

			this.addToBasket(basket, basketItem);
			session.setAttribute("basket", basket);
		}
		
		return "redirect:/basket";
	}

	private void addToBasket(Basket basket, BasketItem newBasketItem) {
		logger.info("addToBasket logic");
		boolean itemAlreadyInBasket = false;

		if (basket.getItemsInBasket().isEmpty()) {
			logger.info("basket is empty");
		}

		for (BasketItem item : basket.getItemsInBasket()) {
			logger.info("basketList: " + item);
			if (item.equals(newBasketItem)) {
				logger.info("addToBasket: item is in basket");
				item.setQuantity(item.getQuantity() + 1);
				itemAlreadyInBasket = true;
			}
		}
		if (itemAlreadyInBasket == false) {
			logger.info("addToBasket: item not in basket");
			basket.getItemsInBasket().add(newBasketItem);
		}
	}

	@RequestMapping("/basket/remove")
	@PreAuthorize("hasRole('ROLE_USER')")
	public String removeItem(@RequestParam int id, HttpSession session) {
		Basket basket = (Basket) session.getAttribute("basket");
		if (basket != null) {
			removeItemFromBasket(basket, id);
			session.setAttribute("basket", basket);
		}

		return "redirect:/basket";
	}

	private void removeItemFromBasket(Basket basket, int id) {
		BasketItem temp = null;
		for (BasketItem basketItem : basket.getItemsInBasket()) {
			if (basketItem.getItem().getId() == (id)) {
				temp = basketItem;
				continue;
			}
		}
		if (temp != null)
			basket.getItemsInBasket().remove(temp);
	}

	@RequestMapping("/basket/update")
	@PreAuthorize("hasRole('ROLE_USER')")
	public String updateBasket(@RequestParam int id, @RequestParam int quantity, HttpSession session) {
		Basket basket = (Basket) session.getAttribute("basket");
		if (basket != null) {
			updateItemInBasket(basket, id, quantity);
			session.setAttribute("basket", basket);
		}
		return "redirect:/basket";
	}
	
	private void updateItemInBasket(Basket basket, int id, int quantity) {
		for (BasketItem basketItem : basket.getItemsInBasket()) {
			if (basketItem.getItem().getId() == (id)) {
				basketItem.setQuantity(quantity);
			}
		}
	}
	
	@RequestMapping("/basket/cancel")
	@PreAuthorize("hasRole('ROLE_USER')")
	public String cancel(HttpSession session, HttpServletRequest request) {
		session.removeAttribute("basket");
		return "redirect:/basket";
		
	}
	
	
	@RequestMapping("/checkout")
	@PreAuthorize("hasRole('ROLE_USER')")
	public String checkout(Principal principal, HttpSession session, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Locale locale = localeResolver.resolveLocale(request);
		Basket basket = (Basket) session.getAttribute("basket");
		
		if (principal != null && basket != null) {
			Order newOrder = new Order();
			newOrder.setStatusCode(OrderStatusCode.Pending);
			newOrder.setOrderName("Order" + UUID.randomUUID());
			
			Set<OrderDetails> orderDetailsList = new HashSet<OrderDetails>();
			
			for (BasketItem bi : basket.getItemsInBasket()) {
				OrderDetails orderDetails = new OrderDetails(bi.getQuantity(), bi.getItem(), newOrder);
				orderDetailsList.add(orderDetails);
			}
			
			newOrder.setOrderDetailsList(orderDetailsList);
			newOrder.setCreatedDate(new Date());
			newOrder.setUser(userService.findByEmail(principal.getName()));
			
			orderService.create(newOrder);
		}
		
		redirectAttributes.addFlashAttribute("message", messageSource.getMessage("success.message.checkout", null, locale));
		redirectAttributes.addFlashAttribute("type", MessageType.success);
		
		session.removeAttribute("basket");
		return "redirect:/";
	}
}
