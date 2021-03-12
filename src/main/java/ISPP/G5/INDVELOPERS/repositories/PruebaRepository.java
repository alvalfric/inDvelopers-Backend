package ISPP.G5.INDVELOPERS.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ISPP.G5.INDVELOPERS.models.Prueba;

@Repository
public interface PruebaRepository extends MongoRepository<Prueba,String> {

}
