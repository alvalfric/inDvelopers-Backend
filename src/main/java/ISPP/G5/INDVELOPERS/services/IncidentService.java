
package ISPP.G5.INDVELOPERS.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Incident;
import ISPP.G5.INDVELOPERS.repositories.IncidentRepository;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class IncidentService {

	@Autowired
	private IncidentRepository repository;

	public List<Incident> findAll() {
		List<Incident> res = repository.findAll();
		res.sort((x,y)->x.getDate().compareTo(y.getDate()));
		return res;
	}

	public Incident findById(final String id) {
		return repository.findById(id).orElse(null);
	}

	public String addIncident(final Incident incident,final Developer developer) {
		Assert.notNull(incident);
		incident.setDeveloper(developer);
		incident.setSolved(false);
		repository.save(incident);
		return "Added Incident with Id: " + incident.getId();
	}

	public String setIncidentAsSolved(final String id) {
		if (!repository.findById(id).isPresent())
			throw new IllegalArgumentException("Error Id: Incident with id " + id + " do not exist");

		Incident incident = repository.findById(id).get();
		incident.setSolved(true);
		repository.save(incident);
		return "Updated Incident with Id: " + incident.getId();
	}

	public String deleteIncident(final String id) {
		if (!repository.findById(id).isPresent())
			throw new IllegalArgumentException("Error Id: Incident with id " + id + " do not exist");

		repository.deleteById(id);
		return "Deleted Incident with Id: " + id;
	}

}
