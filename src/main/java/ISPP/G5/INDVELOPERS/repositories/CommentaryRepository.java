package ISPP.G5.INDVELOPERS.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import ISPP.G5.INDVELOPERS.models.Commentary;

@Repository
public interface CommentaryRepository extends MongoRepository<Commentary, String> {

	Optional<Commentary> findById(String id);
	
	@Query("{'foro.id': ?0}")
	List<Commentary> findByForumId(String forumId);
	
	@Query("{ 'developer.id' : ?0 }")
	List<Commentary> findByDeveloper(String developerId);
}
