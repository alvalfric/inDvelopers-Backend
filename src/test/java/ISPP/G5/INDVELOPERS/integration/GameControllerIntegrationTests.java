package ISPP.G5.INDVELOPERS.integration;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import ISPP.G5.INDVELOPERS.controllers.GameController;
import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.repositories.DeveloperRepository;
import ISPP.G5.INDVELOPERS.repositories.GameRepository;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.GameService;

@SpringBootTest
public class GameControllerIntegrationTests {
	

	@Autowired
	protected DeveloperService developerService;

	@Autowired
	protected GameService gameService;
	
	@Autowired
	protected DeveloperRepository developerRepository;
	
	@Autowired
	protected GameRepository gameRepository;
	
	@Autowired
	private GameController gameController;

	Developer			developer1;
	Game				game1;
	Developer			developer2;
	Game				game2;

	
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

		Game firstGame = new Game("Game1", "Description1", "Requirements1", 0.0, "idCloud1", true, dev1);

		gameRepository.save(firstGame);

		Game secondGame = new Game("Game2", "description2", "requirements2", 10.0, "idCloud2", true, dev2);

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
	@WithMockUser(username = "developer1", authorities = { "USER" })
	@DisplayName("Show all games test")
	void testFindAllGames() throws Exception {
		ResponseEntity<List<Game>> responseListGame = this.gameController.findAll();
		Assertions.assertEquals(responseListGame.getStatusCodeValue(), 200);
		Assertions.assertNotNull(responseListGame.getBody());
		Assertions.assertEquals(responseListGame.getBody().get(0).getTitle(), "Game1");
	}
	
	@Test
	@DisplayName("Add game test")
	@WithMockUser(username = "developer1", authorities = { "USER" })
	void testAddGame() throws Exception {
		Game res = new Game("GameRes", "DescriptionRes", "RequirementsRes", 0.0, "idCloudRes", true, null);
		ResponseEntity<String> responseEntityStringGame = this.gameController.addGame(res);
		Assertions.assertEquals(responseEntityStringGame.getStatusCodeValue(), 201);
		Assertions.assertNotNull(responseEntityStringGame.getBody());
		Assertions.assertEquals(responseEntityStringGame.getBody(), "Added game with title:"+ res.getTitle());
	}
	
	@Test
	@DisplayName("Fail add game not free by developer not premium test")
	@WithMockUser(username = "developer1", authorities = { "USER" })
	void testAddGameNotFree() throws Exception {
		Game res = new Game("GameRes", "DescriptionRes", "RequirementsRes", 10.0, "idCloudRes", true, null);
		ResponseEntity<String> responseEntityStringGame = this.gameController.addGame(res);
		Assertions.assertEquals(responseEntityStringGame.getStatusCodeValue(), 400);
	}
	
	@Test
	@DisplayName("Fail add game with clone title test")
	@WithMockUser(username = "developer1", authorities = { "USER" })
	void testAddGameCloneTitle() throws Exception {
		Game res = new Game("Game2", "DescriptionRes", "RequirementsRes", 0.0, "idCloudRes", true, null);
		ResponseEntity<String> responseEntityStringGame = this.gameController.addGame(res);
		Assertions.assertEquals(responseEntityStringGame.getStatusCodeValue(), 400);
	}
	
	@Test
	@DisplayName("Update game test")
	@WithMockUser(username = "developer1", authorities = { "USER" })
	void testUpdateGame() throws Exception{
		Game res = game1;
		res.setDescription("Descripcion actualizada");
		ResponseEntity<String> responseEntityStringGame = this.gameController.updateGame(game1.getId(), res);
		Assertions.assertEquals(responseEntityStringGame.getStatusCodeValue(), 200);
		Assertions.assertNotNull(responseEntityStringGame.getBody());
		Assertions.assertEquals(responseEntityStringGame.getBody(), "Updated game with title:"+ res.getTitle());
	}
	
	@Test
	@DisplayName("Fail update game by develop not creator test")
	@WithMockUser(username = "developer2", authorities = { "USER" })
	void testUpdateGameByNotCreator() throws Exception{
		Game res = game1;
		res.setDescription("Descripcion actualizada");
		ResponseEntity<String> responseEntityStringGame = this.gameController.updateGame(game1.getId(), res);
		Assertions.assertEquals(responseEntityStringGame.getStatusCodeValue(), 400);
	}
	
	@Test
	@DisplayName("Fail update game with clone title test")
	@WithMockUser(username = "developer1", authorities = { "USER" })
	void testUpdateGameCloneTitle() throws Exception {
		Game res = new Game("Game2", "DescripcionActualizada", "RequirementsRes", 0.0, "idCloudRes", true, null);
		ResponseEntity<String> responseEntityStringGame = this.gameController.updateGame(game1.getId(), res);
		Assertions.assertEquals(responseEntityStringGame.getStatusCodeValue(), 400);
	}
	
	@Test
	@DisplayName("Delete game test")
	@WithMockUser(username = "developer1", authorities = { "USER" })
	void testDeleteGame() throws Exception{
		String res = game1.getId();
		ResponseEntity<HttpStatus> responseEntityHttpStatusGame = this.gameController.deleteGameById(res);
		Assertions.assertEquals(responseEntityHttpStatusGame.getStatusCodeValue(), 204);
		Assertions.assertEquals(this.gameService.findById(res), null);
	}
	
	@Test
	@DisplayName("Fail delete game by not creator test")
	@WithMockUser(username = "developer2", authorities = { "USER" })
	void testDeleteGameByNotCreator() throws Exception{
		String res = game1.getId();
		ResponseEntity<HttpStatus> responseEntityHttpStatusGame = this.gameController.deleteGameById(res);
		Assertions.assertEquals(responseEntityHttpStatusGame.getStatusCodeValue(), 400);
	}
	
	@Test
	@DisplayName("Find game by title")
	@WithMockUser(username = "developer1", authorities = { "USER" })
	void testFindGameByTitle() throws Exception{
		ResponseEntity<List<Game>> responseListGame = this.gameController.getGameByTitle("Game1");
		Assertions.assertEquals(responseListGame.getStatusCodeValue(), 200);
		Assertions.assertNotNull(responseListGame.getBody());
		Assertions.assertEquals(responseListGame.getBody().get(0).getTitle(), "Game1");
	}
	
	@Test
	@DisplayName("Find game by ID")
	@WithMockUser(username = "developer1", authorities = { "USER" })
	void testFindGameByID() throws Exception{
		ResponseEntity<Game> responseGame = this.gameController.getGameById(game1.getId());
		Assertions.assertEquals(responseGame.getStatusCodeValue(), 200);
		Assertions.assertEquals(responseGame.getBody(), game1);
	}
	
	@Test
	@DisplayName("Find game by developer")
	@WithMockUser(username = "developer1", authorities = { "USER" })
	void testFindGameByDeveloper() throws Exception{
		ResponseEntity<List<Game>> responseListGame = this.gameController.getGameByDeveloper(developer1.getUsername());
		Assertions.assertEquals(responseListGame.getStatusCodeValue(), 200);
		Assertions.assertNotNull(responseListGame.getBody());
		Assertions.assertEquals(responseListGame.getBody().get(0).getTitle(), "Game1");
	}
	
	@Test
	@DisplayName("Find game by my games")
	@WithMockUser(username = "developer1", authorities = { "USER" })
	void testFindGameByMyGames() throws Exception{
		ResponseEntity<List<Game>> responseListGame = this.gameController.getGameByMyGames();
		Assertions.assertEquals(responseListGame.getStatusCodeValue(), 200);
		Assertions.assertNotNull(responseListGame.getBody());
		Assertions.assertEquals(responseListGame.getBody().get(0).getTitle(), "Game1");
	}
	
	
}

