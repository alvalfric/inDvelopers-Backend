
package ISPP.G5.INDVELOPERS.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.OwnedGame;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.GameService;
import ISPP.G5.INDVELOPERS.services.OwnedGameService;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class OwnedGameServiceTests {

	@Autowired
	protected OwnedGameService	ownedGameService;

	@Autowired
	protected DeveloperService	developerService;

	@Autowired
	protected GameService		gameService;

	private Developer			alvaro;
	private Developer			dummyDeveloper;
	private Game				game1;
	private Game				game2;


	@BeforeEach
	void init() throws NotFoundException {
		alvaro = developerService.findByUsername("alvaro");
		dummyDeveloper = developerService.findByUsername("dummyDeveloper");
		game1 = gameService.findByTitle("25 caminos oscuros").get(0);
		game2 = gameService.findByTitle("Payaso que salta").get(0);
	}

	@Test
	void shouldFindOwnedGames() {
		OwnedGame ownedGameAlvaro = ownedGameService.findByDeveloper(alvaro);
		List<Game> games = new ArrayList<Game>();
		games.add(game1);
		assertThat(ownedGameAlvaro.getBuyer()).isEqualTo(alvaro);
		assertThat(ownedGameAlvaro.getOwnedGames()).isEqualTo(new ArrayList<Game>(games));
	}

	@Test
	void shouldNotFindOwnedGames() {
		OwnedGame ownedGameDummyDeveloper = ownedGameService.findByDeveloper(dummyDeveloper);
		assertThat(ownedGameDummyDeveloper.getBuyer()).isEqualTo(dummyDeveloper);
		assertThat(ownedGameDummyDeveloper.getOwnedGames()).isEqualTo(new ArrayList<Game>());
	}

	@Test
	void shouldBuyAGameForUserWithPurchasedGame() throws NotFoundException {
		OwnedGame ownedGameAlvaro = ownedGameService.findByDeveloper(alvaro);
		List<Game> games = new ArrayList<Game>();
		games.add(game1);
		assertThat(ownedGameAlvaro.getBuyer()).isEqualTo(alvaro);
		assertThat(ownedGameAlvaro.getOwnedGames()).isEqualTo(games);

		assertThat(ownedGameService.buyGameByDeveloperAndGameId(alvaro, game2.getId()).equals("Buyed game with title: " + game2.getTitle()));

		ownedGameAlvaro = ownedGameService.findByDeveloper(alvaro);
		games.add(game2);
		assertThat(ownedGameAlvaro.getOwnedGames()).isEqualTo(games);
	}

	@Test
	void shouldBuyAGameForUserWithoutGames() throws NotFoundException {
		OwnedGame ownedGameDummyDeveloper = ownedGameService.findByDeveloper(dummyDeveloper);
		List<Game> games = new ArrayList<Game>();
		games.add(game1);
		assertThat(ownedGameDummyDeveloper.getBuyer()).isEqualTo(dummyDeveloper);
		assertThat(ownedGameDummyDeveloper.getOwnedGames()).isEqualTo(new ArrayList<Game>());

		assertThat(ownedGameService.buyGameByDeveloperAndGameId(dummyDeveloper, game1.getId()).equals("Buyed game with title: " + game1.getTitle()));

		ownedGameDummyDeveloper = ownedGameService.findByDeveloper(dummyDeveloper);
		assertThat(ownedGameDummyDeveloper.getOwnedGames()).isEqualTo(games);
	}

	@Test
	void shouldNotBuyTwoGamesWithSameTitle() throws NotFoundException {
		OwnedGame ownedGameAlvaro = ownedGameService.findByDeveloper(alvaro);
		List<Game> games = new ArrayList<Game>();
		games.add(game1);
		assertThat(ownedGameAlvaro.getBuyer()).isEqualTo(alvaro);
		assertThat(ownedGameAlvaro.getOwnedGames()).isEqualTo(games);

		assertThat(ownedGameService.buyGameByDeveloperAndGameId(alvaro, game2.getId()).equals("Buyed game with title: " + game2.getTitle()));

		games.add(game2);
		ownedGameAlvaro = ownedGameService.findByDeveloper(alvaro);
		assertThat(ownedGameAlvaro.getOwnedGames()).isEqualTo(games);

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			ownedGameService.buyGameByDeveloperAndGameId(alvaro, game2.getId());
		});
	}

	@Test
	void shouldThrowExceptionIllegalArgumentExceprtion() throws NotFoundException {
		OwnedGame ownedGameAlvaro = ownedGameService.findByDeveloper(alvaro);
		List<Game> games = new ArrayList<Game>();
		games.add(game1);
		assertThat(ownedGameAlvaro.getBuyer()).isEqualTo(alvaro);
		assertThat(ownedGameAlvaro.getOwnedGames()).isEqualTo(games);

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			ownedGameService.buyGameByDeveloperAndGameId(alvaro, "estejuegonoexiste");
		});
	}

	@Test
	void shouldFindListOfOwnedGames() throws NotFoundException {
		List<Game> games = ownedGameService.findAllMyOwnedGames(alvaro);
		List<Game> checkListGames = new ArrayList<Game>();
		checkListGames.add(game1);
		assertThat(games).isEqualTo(checkListGames);
	}

	@Test
	void shouldCheckOwnedGameTrue() throws NotFoundException {
		boolean checkOwnedGame = ownedGameService.checkGameOwned(alvaro, game1.getId());
		assertTrue(checkOwnedGame);
	}

	@Test
	void shouldCheckOwnedGameFalse() throws NotFoundException {
		boolean checkOwnedGame = ownedGameService.checkGameOwned(alvaro, game2.getId());
		assertFalse(checkOwnedGame);
	}

	@Test
	void shouldCheckOwnedGameWrongGameId() throws NotFoundException {
		assertTrue(ownedGameService.checkGameOwned(alvaro, "estejuegonoexiste"));
	}
}
