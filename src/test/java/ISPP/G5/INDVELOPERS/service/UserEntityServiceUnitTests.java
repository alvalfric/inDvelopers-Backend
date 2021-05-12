
package ISPP.G5.INDVELOPERS.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import ISPP.G5.INDVELOPERS.models.UserEntity;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.repositories.UserEntityRepository;
import ISPP.G5.INDVELOPERS.services.UserEntityService;

@SpringBootTest
public class UserEntityServiceUnitTests {

	@InjectMocks
	UserEntityService		service;

	@Mock
	UserEntityRepository	repository;

	UserEntity				user	= new UserEntity("username", "password", "email", new HashSet<UserRole>(), true);


	@Test
	@DisplayName("Finding all users")
	void testFindAll() {
		when(repository.findAll()).thenReturn(new ArrayList<UserEntity>());

		assertTrue(service.getAll().isEmpty());
	}

	@Test
	@DisplayName("Create user")
	void testCreateUser() {
		when(repository.findByEmail(any(String.class))).thenReturn(Optional.empty());
		when(repository.findByUsername(any(String.class))).thenReturn(Optional.empty());
		when(repository.save(any(UserEntity.class))).thenReturn(user);

		assertNotNull(service.createUser(user));
	}

	@Test
	@DisplayName("Update user")
	void testUpdateUser() {
		when(repository.save(any(UserEntity.class))).thenReturn(user);

		assertNotNull(service.updateUser(user));
	}

	@Test
	@DisplayName("Find by email")
	void testFindByEmail() {
		when(repository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

		assertNotNull(service.findByEmail("email"));
	}

	@Test
	@DisplayName("Find by username")
	void testFindByUsername() {
		when(repository.findByUsername(any(String.class))).thenReturn(Optional.of(user));

		assertNotNull(service.findByUsername("username"));
	}
}
