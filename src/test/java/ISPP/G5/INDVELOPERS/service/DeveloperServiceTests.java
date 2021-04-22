package ISPP.G5.INDVELOPERS.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.catalina.User;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.UserRole;
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
	
	@Test
	@DisplayName("Create a new developer")
	void testCreateDeveloper() {
		Developer d2 = new Developer("developer2", "password2", "email2@gmail.com", null, null , null, "description2", null, null);
		
		when(this.repo.save(d2)).thenReturn(d2);
		Developer res = this.developerService.createDeveloper(d2);
		
		assertTrue(d2.getUsername().equals(res.getUsername()));
	}
	
	@Test
	@DisplayName("Change developer to Admin")
	void testDeveloperToAdmin() {
		Developer d3 = new Developer("developer3", "password3", "email3@gmail.com", null, 
				Stream.of(UserRole.USER).collect(Collectors.toSet()), "description3", null, null, new ArrayList<Developer>());
		d3.setId("IdPrueba3");
		when(this.repo.findById("IdPrueba3")).thenReturn(Optional.of(d3));
		when(this.repo.save(d3)).thenReturn(d3);
		
		Developer res = this.developerService.changeToAdmin("IdPrueba3");
		
		assertTrue(res.getUsername().equals(d3.getUsername()));
		assertTrue(res.getRoles().contains(UserRole.ADMIN));
	}
	
	@Test
	@DisplayName("Change Admin to developer")
	void testAdminToDeveloper() {
		Developer d4 = new Developer("developer4", "password4", "email4@gmail.com", null, 
				Stream.of(UserRole.ADMIN).collect(Collectors.toSet()), "description4", null, null, new ArrayList<Developer>());
		d4.setId("IdPrueba4");
		when(this.repo.findById("IdPrueba4")).thenReturn(Optional.of(d4));
		when(this.repo.save(d4)).thenReturn(d4);
		
		Developer res = this.developerService.changeToUser("IdPrueba4");
		
		assertTrue(res.getUsername().equals(d4.getUsername()));
		assertFalse(res.getRoles().contains(UserRole.ADMIN));
	}
	
	@Test
	@DisplayName("Update a developer")
	void testUpdateDeveloper() {
		Developer d2 = new Developer("developer", "password2", "email2@gmail.com", null, null , null, "description2", null, null);
		
		when(this.repo.save(d2)).thenReturn(d2);
		Developer res = this.developerService.updateDeveloper(d2);
		
		assertTrue(d2.getUsername().equals(res.getUsername()));
	}

}
