package ISPP.G5.INDVELOPERS.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;

@Repository
public interface GameRepository extends MongoRepository<Game,String>{

	@Query("{ 'title' : ?0 }")
	Optional<Game> findByTitle(String title);
	
	@Query("{ 'creator' : ?0 }")
	List<Game> findByDeveloper(Developer developer);
	
	@Query("{ 'creator' : ?0 }")
	List<Game> findByMyGames(Developer developer);
	
	@Override
	List<Game> findAll() throws DataAccessException;
	
	@Query("{ 'id' : ?0 }")
	Optional<Game> findById(String id);
}
