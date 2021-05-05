package ISPP.G5.INDVELOPERS.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ISPP.G5.INDVELOPERS.models.Forum;

@Repository
public interface ForumRepository extends MongoRepository<Forum, String>{

	
	Optional<Forum> findById(String id);
}
