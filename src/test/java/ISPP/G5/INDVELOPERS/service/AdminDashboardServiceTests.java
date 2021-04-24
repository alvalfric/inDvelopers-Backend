
package ISPP.G5.INDVELOPERS.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

import ISPP.G5.INDVELOPERS.models.AdminDashboard;
import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.OwnedGame;
import ISPP.G5.INDVELOPERS.models.Publication;
import ISPP.G5.INDVELOPERS.models.Review;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.repositories.DeveloperRepository;
import ISPP.G5.INDVELOPERS.repositories.GameRepository;
import ISPP.G5.INDVELOPERS.repositories.IncidentRepository;
import ISPP.G5.INDVELOPERS.repositories.OwnedGameRepository;
import ISPP.G5.INDVELOPERS.repositories.PublicationRepository;
import ISPP.G5.INDVELOPERS.repositories.ReviewRepository;
import ISPP.G5.INDVELOPERS.services.AdminDashboardService;
import ISPP.G5.INDVELOPERS.services.DeveloperService;

@SpringBootTest
class AdminDashboardServiceTests {

	@InjectMocks
	AdminDashboardService	service;

	@Mock
	GameRepository			gameRepo;
	@Mock
	PublicationRepository	publicationRepo;
	@Mock
	ReviewRepository		reviewRepo;
	@Mock
	OwnedGameRepository		ownedGameRepo;
	@Mock
	private DeveloperRepository developerRepository;
	@Mock
	private DeveloperService developerService;
	@Mock
	private IncidentRepository incidentRepository;

	// Casos positivos
	@Test
	@DisplayName("Showing Dashboard")
	void testShow() throws DataAccessException, IllegalAccessException {
		Set<UserRole> setRole2 = new HashSet<UserRole>();
		setRole2.add(UserRole.USER);
		setRole2.add(UserRole.ADMIN);
		Developer admin = new Developer("admin", "admin", "admin@gmail.com", null, setRole2, null, "admin", null, null, null);
		when(gameRepo.findAll()).thenReturn(new ArrayList<Game>());
		when(publicationRepo.findAll()).thenReturn(new ArrayList<Publication>());
		when(reviewRepo.findAll()).thenReturn(new ArrayList<Review>());
		when(ownedGameRepo.findAll()).thenReturn(new ArrayList<OwnedGame>());
		when(this.developerService.findCurrentDeveloper()).thenReturn(admin);
		AdminDashboard res = service.show();

		assertNotNull(res);
	}

}
