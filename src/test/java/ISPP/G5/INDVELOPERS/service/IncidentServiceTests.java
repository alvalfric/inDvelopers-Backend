package ISPP.G5.INDVELOPERS.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Incident;
import ISPP.G5.INDVELOPERS.repositories.IncidentRepository;
import ISPP.G5.INDVELOPERS.services.IncidentService;

@SpringBootTest
class IncidentServiceTests {

	@Test
	void contextLoads() {
	}

	
	IncidentRepository repo = mock(IncidentRepository.class);

	IncidentService incidentService = new IncidentService(repo);

	Developer d1;
	Incident i1, i2;

	@BeforeEach
	void initAll() {
		d1 = new Developer("developer1", "password1", "email1@gmail.com", null, null , null, "description1", null, null, null);
		i1 = new Incident("No", "Que no", "Ne", LocalDate.of(2021, 4, 23), null, false, d1);
		i2 = new Incident("No2", "Que no2", "Ne2", LocalDate.of(2021, 4, 23), null, false, d1);

	}

	// Casos positivos
	@Test
	@DisplayName("Finding all incidents")
	void testFindAll() throws DataAccessException {
		Collection<Incident> res;

		when(this.repo.findAll()).thenReturn(Lists.list(i1, i2));

		res = this.incidentService.findAll();

		assertTrue(!res.isEmpty());
		assertTrue(res.size() == 2);
	}

	@Test
	@DisplayName("Finding by Id")
	void testFindById() throws DataAccessException, NotFoundException {
		i1.setId("IdPrueba1");

		when(this.repo.findById("IdPrueba1")).thenReturn(Optional.of(i1));

		Incident res = this.incidentService.findById("IdPrueba1");

		assertTrue(res.getTitle() == "No");
	}
	
	@Test
	@DisplayName("Finding unsolved")
	void testFindUnsolved() throws DataAccessException, NotFoundException {

		when(this.repo.findNotSolved()).thenReturn(Lists.list(i1,i2));

		List<Incident> res = this.incidentService.findNotSolved();

		assertTrue(res.get(0).getTitle() == "No");
	}

	@Test
	@DisplayName("Find by ID doesn't exist")
	void testFindbybadId() throws DataAccessException {
		String badId = "badId";
		when(repo.findById(badId)).thenReturn(null);
		assertThrows(NullPointerException.class,()->this.incidentService.findById(badId));
	}
	
	@Test
	@DisplayName("Create a new incident")
	void testCreateIncident() {
		Incident i3 = new Incident("No3", "Que no3", "Ne3", LocalDate.of(2021, 4, 23), null, false, d1);
		
		when(this.repo.save(i3)).thenReturn(i3);		
		assertTrue(i3.getTitle().equals("No3"));
	}
	
	@Test
	@DisplayName("Solve incident")
	void testSolveIncident() {
		
		when(this.repo.findById("IdPrueba1")).thenReturn(Optional.of(i1));
		
		this.incidentService.setIncidentAsSolved("IdPrueba1");
		
		assertTrue(i1.isSolved() == true);
	}
}
