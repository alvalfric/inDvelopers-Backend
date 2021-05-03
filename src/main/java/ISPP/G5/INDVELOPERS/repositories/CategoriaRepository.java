package ISPP.G5.INDVELOPERS.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import ISPP.G5.INDVELOPERS.models.Category;


@Repository
public interface CategoriaRepository extends MongoRepository<Category, String>{

	@Query("{ 'title' : ?0 }")
	List<Category> findByTitle(String title);
	
}
