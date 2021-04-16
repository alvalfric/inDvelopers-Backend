package ISPP.G5.INDVELOPERS.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.repositories.DeveloperRepository;
import ISPP.G5.INDVELOPERS.repositories.GameRepository;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.GameService;

@SpringBootTest
public class GameServiceTests {

	@Autowired
	protected DeveloperService developerService;

	@Autowired
	protected GameService gameService;

	@Autowired
	protected DeveloperRepository developerRepository;

	@Autowired
	protected GameRepository gameRepository;

	Developer developer1;
	Developer developer2;
	Game game1;
	Game game2;

	@BeforeEach
	void initAll() throws NotFoundException {
		developerRepository.deleteAll();
		gameRepository.deleteAll();
		Developer dev1 = new Developer("developer1", "developer1", "developer1@gmail.com", null, null,
				Stream.of(UserRole.USER).collect(Collectors.toSet()), null, null, false);

		developerRepository.save(dev1);

		Developer dev2 = new Developer("developer2", "developer2", "developer2Developer@gmail.com", null, null,
				Stream.of(UserRole.USER).collect(Collectors.toSet()), null, null, true);

		developerRepository.save(dev2);

		Game firstGame = new Game("Game1", "Description1", "Requirements1", 0.0, "idCloud1", true, dev1,
				"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");

		gameRepository.save(firstGame);

		Game secondGame = new Game("Game2", "description2", "requirements2", 10.0, "idCloud2", true, dev2,
				"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");

		gameRepository.save(secondGame);

		developer1 = this.developerService.findByUsername("developer1");
		developer2 = this.developerService.findByUsername("developer2");
		game1 = this.gameService.findByTitle("Game1").get(0);
		game2 = this.gameService.findByTitle("Game2").get(0);
	}

	@AfterEach
	void endAll() {
		developerRepository.deleteAll();
		gameRepository.deleteAll();
	}

	@Test
	@DisplayName("Show game by id test")
	void shouldFindGameById() throws NotFoundException {
		Game testGame = this.gameService.findById(game1.getId());
		assertThat(testGame).isEqualTo(game1);
	}

	@Test
	@DisplayName("Fail show game by inexistent id test")
	void shouldNotFindGameByInexistentId() throws NotFoundException {
		assertNull(this.gameService.findById("unidinexistente"));
	}

	@Test
	@DisplayName("Show all games test")
	void shouldFindAll() {
		List<Game> testGames = this.gameService.findAll();
		assertThat(testGames).contains(game1);
		assertThat(testGames).contains(game2);
	}

	@Test
	@DisplayName("Add game test")
	void shouldAddGame() throws NotFoundException {
		Game testGame = new Game("testGame", "testGameDescription", "testGameRequirements", 0.0, "testGameIdCloud",
				true, null, null);
		this.gameService.addGame(testGame, developer1);
		assertThat(this.gameService.findAll().contains(testGame)).isNotNull();
	}

	@Test
	@DisplayName("Fail add game by null game test")
	void shouldNotAddGameNullGame() {
		Game testGame = null;
		assertThatThrownBy(() -> this.gameService.addGame(testGame, developer1))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("Show games by title test")
	void shouldFindGameByTitle() {
		Game testGame = new Game("testGame", "testGameDescription", "testGameRequirements", 0.0, "testGameIdCloud",
				true, null, null);
		this.gameService.addGame(testGame, developer1);
		assertThat(this.gameService.findByTitle("testGame").get(0)).isEqualTo(testGame);
	}

	@Test
	@DisplayName("Fail show games by inexistent title test")
	void shouldNotFindGameByTitle() {
		assertThat(this.gameService.findByTitle("untituloinexistente").size() == 0);
	}

	@Test
	@DisplayName("Show games by developer test")
	void shouldFindGameByDeveloper() {
		assertThat(this.gameService.findByDeveloper(developer1.getId())).contains(game1);
	}

	@Test
	@DisplayName("Fail show games by developer inexistent developer test")
	void shouldNotFindGameByDeveloper() {
		assertThat(this.gameService.findByDeveloper("unidinexistente")).isEmpty();
		Assertions.assertFalse(this.gameService.findByDeveloper(developer1.getId()).contains(game2));
	}

	@Test
	@DisplayName("Show games by my games test")
	void shouldFindGameByMyGames() {
		assertThat(this.gameService.findByDeveloper(developer1.getId())).contains(game1);
	}

	@Test
	@DisplayName("Fail show games by my games test")
	void shouldNotFindGameByMyGames() {
		Assertions.assertFalse(this.gameService.findByDeveloper(developer1.getId()).contains(game2));
	}

	@Test
	@DisplayName("Update game test")
	void shouldUpdateGame() {
		Game testGame = new Game("testGame", "testGameDescription", "testGameRequirements", 0.0, "testGameIdCloud",
				true, developer1,
				"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");
		this.gameService.updateGame(testGame);
		assertThat(this.gameService.findAll().contains(testGame)).isNotNull();
	}

	@Test
	@DisplayName("Fail update game by null game test")
	void shouldNotUpdateGameNullGame() {
		Game testGame = null;
		assertThatThrownBy(() -> this.gameService.updateGame(testGame)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("Delete game test")
	void shouldDeleteGame() {
		this.gameService.deleteGame(game1.getId());
		Assertions.assertFalse(this.gameService.findAll().contains(game1));
	}

	@Test
	@DisplayName("Delete game by null game test")
	void shouldNotDeleteGameNotFoundException() {
		assertThatThrownBy(() -> this.gameService.deleteGame(null)).isInstanceOf(IllegalArgumentException.class);
	}
}
