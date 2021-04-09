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

		Game firstGame = new Game("Game1", "Description1", "Requirements1", 0.0, "idCloud1", true, dev1, null);

		gameRepository.save(firstGame);

		Game secondGame = new Game("Game2", "description2", "requirements2", 10.0, "idCloud2", true, dev2, null);

		gameRepository.save(secondGame);
		
		developer1 =  this.developerService.findByUsername("developer1");
		developer2 =  this.developerService.findByUsername("developer2");
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
		assertThatThrownBy(() -> this.gameService.addGame(testGame, developer1)).isInstanceOf(IllegalArgumentException.class);
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
	void shouldNotFindGameByTitle(){
		assertThat(this.gameService.findByTitle("untituloinexistente").size() == 0);
	}
	
	@Test
	@DisplayName("Show games by developer test")
	void shouldFindGameByDeveloper(){
		assertThat(this.gameService.findByDeveloper(developer1.getId())).contains(game1);
	}
	
	@Test
	@DisplayName("Fail show games by developer inexistent developer test")
	void shouldNotFindGameByDeveloper(){
		assertThat(this.gameService.findByDeveloper("unidinexistente")).isEmpty();
		Assertions.assertFalse(this.gameService.findByDeveloper(developer1.getId()).contains(game2));
	}

	@Test
	@DisplayName("Show games by my games test")
	void shouldFindGameByMyGames(){
		assertThat(this.gameService.findByDeveloper(developer1.getId())).contains(game1);
	}
	
	@Test
	@DisplayName("Fail show games by my games test")
	void shouldNotFindGameByMyGames(){
		Assertions.assertFalse(this.gameService.findByDeveloper(developer1.getId()).contains(game2));
	}
	
	@Test
	@DisplayName("Update game test")
	void shouldUpdateGame(){
		Game testGame = new Game("testGame", "testGameDescription", "testGameRequirements", 0.0, "testGameIdCloud",
				true, developer1, null);
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
	void shouldDeleteGame(){
		this.gameService.deleteGame(game1.getId());
		Assertions.assertFalse(this.gameService.findAll().contains(game1));
	}
	
	@Test
	@DisplayName("Delete game by null game test")
	void shouldNotDeleteGameNotFoundException(){
		assertThatThrownBy(() -> this.gameService.deleteGame(null)).isInstanceOf(IllegalArgumentException.class);
	}
}
