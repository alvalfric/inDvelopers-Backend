package ISPP.G5.INDVELOPERS.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ISPP.G5.INDVELOPERS.models.BaseEntity;

@Repository
public interface repository extends MongoRepository<BaseEntity,String> {
	
	
	/*
	 * Ejemplo de repo, no usar y borrar cuando se añadan aqui los repos
	 */

}
