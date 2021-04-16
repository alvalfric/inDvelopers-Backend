package ISPP.G5.INDVELOPERS.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import ISPP.G5.INDVELOPERS.models.DeveloperSubscription;

@Repository
public interface DeveloperSubscriptionRepository extends MongoRepository<DeveloperSubscription,String>{
	
	@Query("{ 'developer.id' : ?0 }")
	Optional<DeveloperSubscription> findByDeveloperId(String buyerId);

}
