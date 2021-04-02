package ISPP.G5.INDVELOPERS.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import ISPP.G5.INDVELOPERS.models.OwnedGame;

@Repository
public interface OwnedGameRepository extends MongoRepository<OwnedGame,String>{
	
	@Query("{ 'buyer.id' : ?0 }")
	OwnedGame findByBuyerId(String buyerId);

}
