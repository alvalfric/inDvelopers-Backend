package ISPP.G5.INDVELOPERS.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ISPP.G5.INDVELOPERS.models.Category;


@Repository
public interface CategoriaRepository extends MongoRepository<Category, String>{

}
