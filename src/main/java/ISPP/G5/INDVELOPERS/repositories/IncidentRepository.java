
package ISPP.G5.INDVELOPERS.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import ISPP.G5.INDVELOPERS.models.Incident;

@Repository
public interface IncidentRepository extends MongoRepository<Incident, String> {

	@Query("{'solved':false}")
	List<Incident> findNotSolved();

}
