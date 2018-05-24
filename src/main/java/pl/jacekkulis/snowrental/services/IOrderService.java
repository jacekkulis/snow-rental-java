package pl.jacekkulis.snowrental.services;

import java.util.List;
import pl.jacekkulis.snowrental.models.Order;
import pl.jacekkulis.snowrental.models.OrderStatusCode;
import pl.jacekkulis.snowrental.models.User;

public interface IOrderService {
	public Order create(Order order);
	public Order delete(int id);
	public Order update(Order order);
	public List<Order> findAll();
	
	public Order findById(int id);
	public Order findByOrderName(String orderName);
	public Order findByStatusCode(OrderStatusCode statusCode);
	public List<Order> findOrdersByUser(User user);
	public void updateStatusCode(int orderId, OrderStatusCode statusCode);
}
