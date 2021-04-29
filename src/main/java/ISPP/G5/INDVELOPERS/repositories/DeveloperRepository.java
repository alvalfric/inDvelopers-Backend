package ISPP.G5.INDVELOPERS.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import ISPP.G5.INDVELOPERS.models.Developer;

@Repository
public interface DeveloperRepository extends MongoRepository<Developer, String>{
	
	Optional<Developer> findByUsername(String username);
	
	Optional<Developer> findByEmail(String email);

	@Override
	List<Developer> findAll() throws DataAccessException;;
	
	@Query("{ 'following.id' : ?0 }")
	List<Developer> findMyFollowers(String id);
	
	@Query("{_id: ObjectId('?0'), roles: 'ADMIN'}")
	Optional<Developer> checkDeveloperIsAdmin(String id);
}
