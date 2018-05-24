package pl.jacekkulis.snowrental.controllers;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.jacekkulis.snowrental.models.Order;
import pl.jacekkulis.snowrental.models.OrderDetails;
import pl.jacekkulis.snowrental.models.User;
import pl.jacekkulis.snowrental.services.IOrderService;
import pl.jacekkulis.snowrental.services.IUserService;
import pl.jacekkulis.snowrental.utils.GeneratePdfReport;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@Controller
public class OrderController {

	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private IUserService userService;
	
	@Autowired
	private IOrderService orderService;
	
	@RequestMapping(value = "/orders", method = RequestMethod.GET)
	public ModelAndView listOrders(HttpServletRequest request, Principal principal) {
		logger.info("listOrders");
		ModelAndView mav = new ModelAndView("order/orderHistory");
		int orderId = ServletRequestUtils.getIntParameter(request, "orderId", -1);
		
		User currentUser = null;
	
		if (principal != null) {
			currentUser = userService.findByEmail(principal.getName());
		}
		
		if (currentUser != null) {
			if (orderId > 0)
				mav.addObject("order", orderService.findById(orderId));
			else
				mav.addObject("order", new Order());

			mav.addObject("orderList", orderService.findOrdersByUser(currentUser));
		}

		return mav;
	}
	
	@RequestMapping(value = "/order/getReport", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional
	public ResponseEntity<InputStreamResource> getPdf(@RequestParam(value = "orderId", required = true) String orderId) throws IOException {
		logger.info("getPdf");
		logger.info("orderId="+orderId);
		Order order = orderService.findById(Integer.valueOf(orderId));
		
		logger.info("order: " + order);
	
		Hibernate.initialize(order.getOrderDetailsList());
		List<OrderDetails> orderDetailsList = new ArrayList<OrderDetails>();
		orderDetailsList.addAll(order.getOrderDetailsList());
		
		for(OrderDetails o : orderDetailsList) {
			logger.info("orderDetails: " + o);
		}
		
		
		ByteArrayInputStream bis = GeneratePdfReport.orderReport(orderDetailsList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=citiesreport.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
