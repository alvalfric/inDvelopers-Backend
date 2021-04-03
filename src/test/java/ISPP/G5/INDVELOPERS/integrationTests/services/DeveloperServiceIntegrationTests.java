
package ISPP.G5.INDVELOPERS.integrationTests.services;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.repositories.DeveloperRepository;
import ISPP.G5.INDVELOPERS.services.DeveloperService;

@SpringBootTest
class DeveloperServiceIntegrationTests {

	@Autowired
	DeveloperRepository	repo;
	@Autowired
	DeveloperService	developerService	= new DeveloperService(null, null, repo);

	Developer			d1;
	Developer			d2;
	Integer				sizeBefore;


	@BeforeEach
	void initAll() {
		sizeBefore = developerService.getAll().size();
		d1 = new Developer("developer1", "password1", "email1@gmail.com", null, null, null, "description1", null, null);
		repo.save(d1);
	}

	@AfterEach
	void end() {
		repo.deleteAll();
	}

	// Casos positivos
	@Test
	@DisplayName("Finding all developers")
	void testFindAll() throws DataAccessException {
		Collection<Developer> res;

		res = developerService.getAll();

		assertTrue(!res.isEmpty());
		assertTrue(res.contains(d1));
	}

	@Test
	@DisplayName("Finding by Id")
	void testFindById() throws DataAccessException, NotFoundException {
		d1.setId("IdPrueba1");
		repo.save(d1);

		Developer res = developerService.findById("IdPrueba1");

		assertTrue(res.getUsername().equals("developer1"));
	}

	@Test
	@DisplayName("Finding by Username")
	void testFindByUsername() {

		Developer res = developerService.findByUsername("developer1");

		assertTrue(res.getEmail().equals("email1@gmail.com"));
	}

	@Test
	@DisplayName("Find by ID doesn't exist")
	void testFindbybadId() {
		String badId = "badId";
		assertNull(developerService.findById(badId));
	}

	@Test
	@DisplayName("Finding by bad Username")
	void testFindBadByUsername() {
		String badUsername = "aaaa";

		assertNull(developerService.findByUsername(badUsername));
	}

}
