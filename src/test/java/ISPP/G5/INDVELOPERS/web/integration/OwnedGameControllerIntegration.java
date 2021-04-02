package ISPP.G5.INDVELOPERS.web.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpStatusCodeException;

import ISPP.G5.INDVELOPERS.controllers.OwnedGameController;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.GameService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OwnedGameControllerIntegration {

	@Autowired
	private OwnedGameController ownedGameController;
	@Autowired
	private DeveloperService developerService;
	@Autowired
	private GameService gameService;

	@WithMockUser(username = "alvaro", authorities = { "USER" })
	@Test
	void testFindAllOwnedGames() throws Exception {
		ResponseEntity<List<Game>> responseListGame = this.ownedGameController.findAll();
		Assertions.assertEquals(responseListGame.getStatusCodeValue(), 200);
		Assertions.assertNotNull(responseListGame.getBody());
		Assertions.assertEquals(responseListGame.getBody().get(0).getTitle(), "25 caminos oscuros");

	}

	@WithMockUser(username = "dummyDeveloper", authorities = { "USER" })
	@Test
	void testFindAllOwnedGamesEmptyList() throws Exception {
		ResponseEntity<List<Game>> responseListGame = this.ownedGameController.findAll();
		Assertions.assertEquals(responseListGame.getStatusCodeValue(), 200);
		Assertions.assertNotNull(responseListGame.getBody());
		Assertions.assertTrue(responseListGame.getBody().isEmpty());
	}

	@WithMockUser(username = "alvaro", authorities = { "USER" })
	@Test
	void testBuyNewGame() throws Exception {
		Game game = this.gameService.findByTitle("Payaso que salta").get(0);
		ResponseEntity<String> responseBuyGame = this.ownedGameController.buyGame(game.getId());
		Assertions.assertEquals(responseBuyGame.getStatusCodeValue(), 201);
		Assertions.assertNotNull(responseBuyGame.getBody());
		Assertions.assertEquals(responseBuyGame.getBody(), "Buyed game with title: Payaso que salta");
	}

	@WithMockUser(username = "alvaro", authorities = { "USER" })
	@Test
	void testBuyAlreadyOwnedGame() throws Exception {
		Game game = this.gameService.findByTitle("25 caminos oscuros").get(0);
		ResponseEntity<String> responseBuyGame = this.ownedGameController.buyGame(game.getId());
		Assertions.assertEquals(responseBuyGame.getStatusCodeValue(), 400);
	}

	@WithMockUser(username = "alvaro", authorities = { "USER" })
	@Test
	void testBuyGameWithWorngId() throws Exception {
		ResponseEntity<String> responseBuyGame = this.ownedGameController.buyGame("wrongid");
		assertEquals(responseBuyGame.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@WithMockUser(username = "alvaro", authorities = { "USER" })
	@Test
	void testCheckOwnedGameTrue() throws Exception {
		Game game = this.gameService.findByTitle("25 caminos oscuros").get(0);
		ResponseEntity<Boolean> responseBuyGame = this.ownedGameController.checkGameOwned(game.getId());
		Assertions.assertEquals(responseBuyGame.getStatusCodeValue(), 200);
		Assertions.assertEquals(responseBuyGame.getBody(), true);
	}
	
	@WithMockUser(username = "alvaro", authorities = { "USER" })
	@Test
	void testCheckOwnedGameFalse() throws Exception {
		Game game = this.gameService.findByTitle("Almas oscuras").get(0);
		ResponseEntity<Boolean> responseBuyGame = this.ownedGameController.checkGameOwned(game.getId());
		Assertions.assertEquals(responseBuyGame.getStatusCodeValue(), 200);
		Assertions.assertEquals(responseBuyGame.getBody(), false);
	}
	
	@WithMockUser(username = "alvaro", authorities = { "USER" })
	@Test
	void testCheckOwnedGameWrongGameId() throws Exception {
		ResponseEntity<Boolean> responseBuyGame = this.ownedGameController.checkGameOwned("wrongid");
		assertEquals(responseBuyGame.getStatusCode(), HttpStatus.OK);

//		Assertions.assertThrows(NotFoundException.class, () -> {
//			this.ownedGameController.checkGameOwned("wrongid");
//		});
	}
}
