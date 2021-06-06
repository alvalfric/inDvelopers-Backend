package ISPP.G5.INDVELOPERS.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import ISPP.G5.INDVELOPERS.models.Commentary;
import ISPP.G5.INDVELOPERS.models.Forum;

@Repository
public interface ForumRepository extends MongoRepository<Forum, String>{

	Optional<Forum> findById(String id);
	
	@Query("{'title': {'$regex': ?0, '$options': 'i'}}")
	List<Forum> findByTitle(String title);
	
	@Query("{ 'developer.id' : ?0 }")
	List<Forum> findByDeveloper(String developerId);

}
