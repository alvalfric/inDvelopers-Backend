package ISPP.G5.INDVELOPERS.paypal;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ISPP.G5.INDVELOPERS.models.Order;

@Repository
public interface OrderRepository extends MongoRepository<Order, String>{

}
