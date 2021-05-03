
package ISPP.G5.INDVELOPERS.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.DeveloperSubscription;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.repositories.DeveloperSubscriptionRepository;
import ISPP.G5.INDVELOPERS.services.DeveloperSubscriptionService;

@SpringBootTest
class DeveloperSubscriptionServiceTests {

	@InjectMocks
	DeveloperSubscriptionService	service;

	@Mock
	DeveloperSubscriptionRepository	repository;

	Developer						developer				= new Developer("hola", "adios", "email", null, new HashSet<UserRole>(), "description", "none", true, null, new ArrayList<Developer>());
	DeveloperSubscription			developerSubscription	= new DeveloperSubscription(developer, LocalDate.now(), LocalDate.of(2022, 5, 10));


	// Casos positivos
	@Test
	@DisplayName("Finding by developer")
	void testFindByDeveloper() throws DataAccessException {
		when(repository.findByDeveloperId(developer.getId())).thenReturn(Optional.of(developerSubscription));

		DeveloperSubscription res = service.findByDeveloper(developer);

		assertNotNull(res);
	}

	@Test
	@DisplayName("Checking developer has Subscription")
	void testCheckDeveloperHasSubscription() throws DataAccessException {
		when(repository.findByDeveloperId(developer.getId())).thenReturn(Optional.of(developerSubscription));

		boolean hasSub = service.checkDeveloperHasSubscription(developer);

		assertTrue(hasSub);
	}

}
