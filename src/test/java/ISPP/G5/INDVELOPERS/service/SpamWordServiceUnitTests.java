
package ISPP.G5.INDVELOPERS.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import ISPP.G5.INDVELOPERS.mappers.DeveloperDTOConverter;
import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.Publication;
import ISPP.G5.INDVELOPERS.models.Review;
import ISPP.G5.INDVELOPERS.models.SpamWord;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.repositories.SpamWordRepository;
import ISPP.G5.INDVELOPERS.services.SpamWordService;

@SpringBootTest
public class SpamWordServiceUnitTests {

	@InjectMocks
	SpamWordService		service;

	@Mock
	SpamWordRepository	repository;


	@Test
	@DisplayName("Finding all non-allowed words")
	void testFindAllNoAllowed() {
		when(repository.findAll()).thenReturn(new ArrayList<SpamWord>());

		assertTrue(service.findAllNoAllowed().isEmpty());
	}

	@Test
	@DisplayName("Is a word spam?")
	void testIsSpam() {
		when(repository.findAll()).thenReturn(new ArrayList<SpamWord>());

		assertTrue(!service.isSpam("hola"));
	}

	@Test
	@DisplayName("Checking game for spam")
	void testCheckGame() {
		Game g = new Game("hola", "hola", "hola", null, null, true, null, null, null, null, null, null, null, null);
		when(repository.findAll()).thenReturn(new ArrayList<SpamWord>());

		assertTrue(!service.CheckGame(g));
	}

	@Test
	@DisplayName("Checking review for spam")
	void testCheckReview() {
		Review r = new Review("hola", 0., null, null, null);
		when(repository.findAll()).thenReturn(new ArrayList<SpamWord>());

		assertTrue(!service.CheckReview(r));
	}

	@Test
	@DisplayName("Checking publication for spam")
	void testCheckPublication() {
		Publication p = new Publication(null, null, "hola", null, null);
		when(repository.findAll()).thenReturn(new ArrayList<SpamWord>());

		assertTrue(!service.CheckPublication(p));
	}

	@Test
	@DisplayName("Checking developer for spam")
	void testCheckDeveloper() {
		Developer d = new Developer("hola", null, null, null, null, "hola", "hola", null, null, null);
		when(repository.findAll()).thenReturn(new ArrayList<SpamWord>());

		assertTrue(!service.CheckDeveloper(d));
	}

	@Test
	@DisplayName("Checking getDeveloperDTO for spam")
	void testCheckGetDeveloperDTO() {
		Developer d = new Developer("hola", null, null, null, new HashSet<UserRole>(), "hola", "hola", null, null, new ArrayList<Developer>());
		when(repository.findAll()).thenReturn(new ArrayList<SpamWord>());

		assertTrue(!service.CheckGetDeveloperDTO(DeveloperDTOConverter.DevelopertoGetDeveloperDTO(d)));
	}
}
