package ISPP.G5.INDVELOPERS.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ISPP.G5.INDVELOPERS.models.UserEntity;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends MongoRepository<UserEntity, String> {
	
	Optional<UserEntity> findByUsername(String username);
	
	Optional<UserEntity> findByEmail(String email);

}
