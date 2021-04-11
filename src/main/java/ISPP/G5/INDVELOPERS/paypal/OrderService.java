package ISPP.G5.INDVELOPERS.paypal;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.models.Order;

@Service
public class OrderService {
	@Autowired
	private OrderRepository repository;
	
	public Order findById(String id) {
		Optional<Order> order = repository.findById(id);
		if (order.get() != null) {
			return order.get();
		} else {
			throw new IllegalArgumentException("This order doesn't exist.");
		}
	}
	
	public Order save(Order order) {
		return this.repository.save(order);
	}
}
