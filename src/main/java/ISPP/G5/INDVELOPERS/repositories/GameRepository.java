package ISPP.G5.INDVELOPERS.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ISPP.G5.INDVELOPERS.models.Game;

@Repository
public interface GameRepository extends MongoRepository<Game,String>{

	//@Query("SELECT g FROM Game g WHERE g.title = ?1")
	Optional<Game> findByTitle(String title);
	

	
	@Override
	List<Game> findAll() throws DataAccessException;
}
