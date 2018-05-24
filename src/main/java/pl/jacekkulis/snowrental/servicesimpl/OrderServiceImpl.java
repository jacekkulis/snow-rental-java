package pl.jacekkulis.snowrental.servicesimpl;

import pl.jacekkulis.snowrental.dao.OrderRepository;
import pl.jacekkulis.snowrental.models.Order;
import pl.jacekkulis.snowrental.models.OrderStatusCode;
import pl.jacekkulis.snowrental.models.User;
import pl.jacekkulis.snowrental.services.IOrderService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements IOrderService {
	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderRepository orderRepository;
	

	@Override
	@Transactional
	public Order create(Order order) {
		logger.info("create");
		Order newOrder = order;
		return orderRepository.save(newOrder);
	}

	@Override
	@Transactional
	public Order delete(int id) {
		logger.info("delete");
		Order order = orderRepository.findById(id);
		orderRepository.delete(order);
		return order;
	}

	@Override
	@Transactional
	public Order update(Order order) {
		logger.info("update");
		return null;
	}

	@Override
	@Transactional
	public List<Order> findAll() {
		logger.info("findAll");
		return orderRepository.findAll();
	}

	@Override
	@Transactional
	public Order findById(int id) {
		logger.info("findById");
		return orderRepository.findById(id);
	}

	@Override
	@Transactional
	public Order findByOrderName(String orderName) {
		logger.info("findByOrderName");
		return orderRepository.findByOrderName(orderName);
	}

	@Override
	@Transactional
	public Order findByStatusCode(OrderStatusCode statusCode) {
		logger.info("findByStatusCode");
		return orderRepository.findByStatusCode(statusCode);
	}

	@Override
	@Transactional
	public List<Order> findOrdersByUser(User user) {
		return orderRepository.findByUser(user);
	}

	@Override
	public void updateStatusCode(int orderId, OrderStatusCode statusCode) {
		Order order = orderRepository.findById(orderId);
		
		if (order != null) {
			order.setStatusCode(statusCode);
			orderRepository.save(order);
		}
		
	}
}