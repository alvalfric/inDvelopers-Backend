
package ISPP.G5.INDVELOPERS.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import ISPP.G5.INDVELOPERS.models.Game;

@Repository
public interface GameRepository extends MongoRepository<Game, String> {

	@Query("{ 'creator.id' : ?0 }")
	List<Game> findByDeveloper(String developerId);

	@Query("{ 'creator.id' : ?0 }")
	List<Game> findByMyGames(String developerId);

	@Override
	List<Game> findAll() throws DataAccessException;

	@Query("{'isNotMalware':true}")
	List<Game>findVerified();
	
	Optional<Game> findById(String id) throws DataAccessException;

	@Query("{'isNotMalware':{'$ne':true}}")
	List<Game> findNotRevised();
	
	@Query("{'title': {'$regex': ?0}, 'isNotMalware':true}")
	List<Game> findByTitleVerified(String res);
	
	@Query("{'price': {'$lt': ?0}, 'isNotMalware':true}")
	List<Game> findByPrice(Double price);

}
