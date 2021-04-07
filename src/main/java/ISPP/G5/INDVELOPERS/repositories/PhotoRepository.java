package ISPP.G5.INDVELOPERS.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ISPP.G5.INDVELOPERS.models.Photo;

@Repository
public interface PhotoRepository extends MongoRepository<Photo, String>{

}
