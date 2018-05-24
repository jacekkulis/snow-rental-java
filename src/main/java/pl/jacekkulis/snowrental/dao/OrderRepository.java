package pl.jacekkulis.snowrental.dao;


import java.util.List;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jacekkulis.snowrental.models.Order;
import pl.jacekkulis.snowrental.models.OrderStatusCode;
import pl.jacekkulis.snowrental.models.User;

@Transactional
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
	Order findByOrderName(String orderName);
	Order findByStatusCode(OrderStatusCode statusCode);
	Order findById(int id);
	List<Order> findByUser(User user);
}
