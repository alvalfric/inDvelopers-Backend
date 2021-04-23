
package ISPP.G5.INDVELOPERS.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.UserRole;
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
		d1 = new Developer("developer1", "password1", "email1@gmail.com", null, null, "description1", null, null, null, new ArrayList<Developer>());
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
	
	@Test
	@DisplayName("Create a new developer")
	void testCreateDeveloper() {
		Developer res = new Developer("developer2", "password2", "email2@gmail.com", null, null , null, "description2", null, null, null);
		
		this.developerService.createDeveloper(res);
		assertThat(this.developerService.findByUsername("developer2")).isNotNull();
	}
	
	@Test
	@DisplayName("Change developer to Admin")
	void testDeveloperToAdmin() {
		Developer d3 = new Developer("developer3", "password3", "email3@gmail.com", null , Stream.of(UserRole.USER).collect(Collectors.toSet()), "description3", null, null, null, new ArrayList<Developer>());
		this.repo.save(d3);
		Developer res = this.developerService.changeToAdmin(d3.getId());
		
		assertTrue(res.getUsername().equals(d3.getUsername()));
		assertTrue(res.getRoles().contains(UserRole.ADMIN));
	}
	
	@Test
	@DisplayName("Change Admin to developer")
	void testAdminToDeveloper() {
		Developer d4 = new Developer("developer4", "password4", "email4@gmail.com", null , Stream.of(UserRole.ADMIN).collect(Collectors.toSet()), "description4", null, null, null, new ArrayList<Developer>());
		this.repo.save(d4);
		Developer res = this.developerService.changeToUser(d4.getId());
		
		assertTrue(res.getUsername().equals(d4.getUsername()));
		assertFalse(res.getRoles().contains(UserRole.ADMIN));
	}
	
	
	@Test
	@DisplayName("Update a developer")
	void testUpdateDeveloper() {
		Developer res = new Developer("developer2", "password2", "email2@gmail.com", null, null , null, "description2", null, null, null);
		
		this.developerService.updateDeveloper(res);
		assertThat(this.developerService.findByUsername("developer2")).isNotNull();
	}

}
