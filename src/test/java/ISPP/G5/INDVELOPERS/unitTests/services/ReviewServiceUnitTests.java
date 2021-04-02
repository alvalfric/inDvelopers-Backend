
package ISPP.G5.INDVELOPERS.unitTests.services;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.Review;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.repositories.ReviewRepository;
import ISPP.G5.INDVELOPERS.services.ReviewService;

@SpringBootTest
public class ReviewServiceUnitTests {

	@InjectMocks
	ReviewService		service;

	@Mock
	ReviewRepository	repository;

	Developer			developer	= new Developer("hola", "adios", "email", new ArrayList<String>(), null, new HashSet<UserRole>(), "description", "none", true);
	Game				game		= new Game("title", "description", "requirements", 2., "iddClou", true, developer);
	Review				r1			= new Review("Text1", 1., game, developer);
	Review				r2			= new Review("Text2", 2., game, developer);


	@Test
	@DisplayName("Finding all reviews test")
	void testFindAll() {
		List<Review> res;
		List<Review> revs = new ArrayList<>();
		revs.add(r1);
		revs.add(r2);
		when(repository.findAll()).thenReturn(revs);

		res = service.findAll();

		assertTrue(!res.isEmpty());
		assertTrue(res.size() == 2);
	}

	@Test
	@DisplayName("Finding review by id test")
	void testFindById() throws NotFoundException {
		r1.setId("IdPrueba1");

		when(repository.findById("IdPrueba1")).thenReturn(Optional.of(r1));

		Review res = service.findById("IdPrueba1");

		assertTrue(res.getScore() == r1.getScore());
		assertTrue(res.getText() == r1.getText());
	}

	@Test
	@DisplayName("Finding reviews by game id test")
	void testFindByGame() {
		List<Review> res;

		when(repository.findAllByGameId(game.getId())).thenReturn(Lists.list(r1, r2));

		res = service.findAllByGameId(game.getId());

		assertTrue(!res.isEmpty());
		assertTrue(res.size() == 2);
	}
}
