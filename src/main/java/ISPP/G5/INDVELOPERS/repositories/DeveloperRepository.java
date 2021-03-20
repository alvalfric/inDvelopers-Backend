package ISPP.G5.INDVELOPERS.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import ISPP.G5.INDVELOPERS.models.Developer;

public interface DeveloperRepository extends MongoRepository<Developer, String>{
	
	Optional<Developer> findByUsername(String username);
	
	Optional<Developer> findByEmail(String email);

}
