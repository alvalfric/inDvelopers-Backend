package ISPP.G5.INDVELOPERS.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.Publication;

@Repository
public interface PublicationRepository extends MongoRepository<Publication, String> {

	@Override
	List<Publication> findAll() throws DataAccessException;

	List<Publication> findByUsername(String username);
	
	Optional<Publication> findById(String id) throws DataAccessException;
	
	@Query("{ 'developer.id' : ?0 }")
	List<Publication> findByDeveloper(String developerId);
}
