package ISPP.G5.INDVELOPERS.ServiceTests;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.repositories.DeveloperRepository;
import ISPP.G5.INDVELOPERS.services.DeveloperService;

@SpringBootTest
class DeveloperServiceTests {

	@Test
	void contextLoads() {
	}

	DeveloperRepository repo = mock(DeveloperRepository.class);

	DeveloperService developerService = new DeveloperService(null, null, repo);

	Developer d1;
	Developer d2;

	@BeforeEach
	void initAll() {
		d1 = new Developer("developer1", "password1", "email1@gmail.com", null, null , null, "description1", null, null);
	}

	// Casos positivos
	@Test
	@DisplayName("Finding all developers")
	void testFindAll() throws DataAccessException {
		Collection<Developer> res;

		when(this.repo.findAll()).thenReturn(Lists.list(d1, d2));

		res = this.developerService.getAll();

		assertTrue(!res.isEmpty());
		assertTrue(res.size() == 2);
	}

	@Test
	@DisplayName("Finding by Id")
	void testFindById() throws DataAccessException, NotFoundException {
		d1.setId("IdPrueba1");

		when(this.repo.findById("IdPrueba1")).thenReturn(Optional.of(d1));

		Developer res = this.developerService.findById("IdPrueba1");

		assertTrue(res.getUsername() == "developer1");
	}

	@Test
	@DisplayName("Finding by Username")
	void testFindByUsername() throws DataAccessException, NotFoundException {
		Developer developerPrueba = new Developer("developerPrueba", "password", "emailPrueba@gmail.com", null, null , null, "descriptionPrueba", null, null);
		developerPrueba.setUsername("test");

		when(this.repo.findByUsername("test")).thenReturn(Optional.of(developerPrueba));

		Developer res = this.developerService.findByUsername("test");

		assertTrue(res.getEmail().equals("emailPrueba@gmail.com"));
	}

	@Test
	@DisplayName("Find by ID doesn't exist")
	void testFindbybadId() throws DataAccessException {
		String badId = "badId";
		when(repo.findById(badId)).thenReturn(null);
		assertThrows(NullPointerException.class,()->this.developerService.findById(badId));
	}
	
	@Test
	@DisplayName("Finding by bad Username")
	void testFindBadByUsername() throws DataAccessException, NotFoundException {
		String badUsername = "aaaa";
		
		when(this.repo.findByUsername(badUsername)).thenReturn(null);
		assertThrows(NullPointerException.class,()->this.developerService.findByUsername(badUsername));
	}

}
