
package ISPP.G5.INDVELOPERS.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.Review;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {

	@Query("{ 'game.id' : ?0 }")
	List<Review> findAllByGameId(String gameId);
	
	@Query("{ 'developer.id' : ?0 }")
	List<Review> findByMyReviews(String developerId);

}
