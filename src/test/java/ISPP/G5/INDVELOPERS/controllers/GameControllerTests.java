package ISPP.G5.INDVELOPERS.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.GameService;

@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerTests {


	@Autowired
	private MockMvc		mvc;

	@MockBean
	DeveloperService	developerService;

	@MockBean
	GameService			gameService;

	Developer			developer1;
	Game				game1;
	Developer			developer2;
	Game				game2;

	
	@BeforeEach
	void setUp() {
		developer1 = new Developer("developer1", "developer1", "developer1@gmail.com", null, null,
				new HashSet<UserRole>(), null, null, false);

		developer1.setId("dev1Id");

		developer2 = new Developer("developer2", "developer2", "developer2Developer@gmail.com", null, null,
				new HashSet<UserRole>(), null, null, true);

		developer2.setId("dev2Id");

		game1 = new Game("Game1", "Description1", "Requirements1", 0.0, "idCloud1", true, developer1, null);

		game1.setId("game1Id");

		game2 = new Game("Game2", "description2", "requirements2", 10.0, "idCloud2", true, developer2, null);

		game2.setId("game2Id");
	}
	
	@Test
	@DisplayName("Show all games test")
	@WithMockUser(value = "spring")
	void testFindAllGames() throws Exception {
		List<Game> games = new ArrayList<>();
		games.add(game1);
		games.add(game2);
		when(gameService.findAll()).thenReturn(games);

		mvc.perform(get("/games/findAll")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value("game1Id"));
	}
	
	@Test
	@DisplayName("Add game test")
	@WithMockUser(value = "spring")
	void testAddGame() throws Exception {
		Game res = new Game("GameRes", "DescriptionRes", "RequirementsRes", 0.0, "idCloudRes", true, developer1, null);
		String bodyContent = objectToJsonStringContent(res);
		
		when(gameService.addGame(any(Game.class), any(Developer.class))).thenReturn("Added Game");
		when(developerService.findByUsername("spring")).thenReturn(developer1);

		mvc.perform(post("/games/add").contentType(MediaType.APPLICATION_JSON).content(bodyContent)).andExpect(status().isCreated()).andExpect(content().string("Added Game"));
	}
	
	@Test
	@DisplayName("Fail add game not free by developer not premium test")
	@WithMockUser(value = "spring")
	void testAddGameNotFree() throws Exception {
		Game res = new Game("GameRes", "DescriptionRes", "RequirementsRes", 10.0, "idCloudRes", true, null, null);
		String bodyContent = objectToJsonStringContent(res);
		
		when(gameService.addGame(any(Game.class), any(Developer.class))).thenReturn("Added Game");
		when(developerService.findByUsername("spring")).thenReturn(developer1);

		mvc.perform(post("/games/add").contentType(MediaType.APPLICATION_JSON).content(bodyContent)).andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("Update game test")
	@WithMockUser(value = "spring")
	void testUpdateGame() throws Exception{
		String bodyContent = objectToJsonStringContent(game2);

		when(gameService.updateGame(any(Game.class))).thenReturn("Edited Game");
		when(gameService.findById("ID")).thenReturn(game2);
		when(developerService.findByUsername("spring")).thenReturn(developer2);

		mvc.perform(put("/games/edit/ID").contentType(MediaType.APPLICATION_JSON).content(bodyContent)).andExpect(status().isOk()).andExpect(content().string("Edited Game"));
	}
	
	@Test
	@DisplayName("Fail update game by develop not creator test")
	@WithMockUser(value = "spring")
	void testUpdateGameByNotCreator() throws Exception{
		String bodyContent = objectToJsonStringContent(game2);

		when(gameService.updateGame(any(Game.class))).thenReturn("Edited Game");
		when(gameService.findById("ID")).thenReturn(game2);
		when(developerService.findByUsername("spring")).thenReturn(developer1);

		mvc.perform(put("/games/edit/ID").contentType(MediaType.APPLICATION_JSON).content(bodyContent)).andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("Delete game test")
	@WithMockUser(value = "spring")
	void testDeleteGame() throws Exception{

		when(gameService.findById("ID")).thenReturn(game2);
		when(developerService.findByUsername("spring")).thenReturn(developer2);

		mvc.perform(delete("/games/delete/ID")).andExpect(status().isNoContent());
	}
	
	@Test
	@DisplayName("Fail delete game by not creator test")
	@WithMockUser(value = "spring")
	void testDeleteGameByNotCreator() throws Exception{

		when(gameService.findById("ID")).thenReturn(game2);
		when(developerService.findByUsername("spring")).thenReturn(developer1);

		mvc.perform(delete("/games/delete/ID")).andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("Find game by title")
	@WithMockUser(value = "spring")
	void testFindGameByTitle() throws Exception{
		List<Game> games = new ArrayList<>();
		games.add(game1);
		
		when(gameService.findByTitle("Game1")).thenReturn(games);

		mvc.perform(get("/games/findByTitle/Game1")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value("game1Id"));
	}
	
	@Test
	@DisplayName("Find game by ID")
	@WithMockUser(value = "spring")
	void testFindGameByID() throws Exception{
		
		when(gameService.findById("ID")).thenReturn(game1);

		mvc.perform(get("/games/ID")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value("game1Id"));
	}
	
	@Test
	@DisplayName("Find game by developer")
	@WithMockUser(value = "spring")
	void testFindGameByDeveloper() throws Exception{
		List<Game> games = new ArrayList<>();
		games.add(game1);
		
		when(developerService.findByUsername("developer1")).thenReturn(developer1);
		when(gameService.findByDeveloper(developer1.getId())).thenReturn(games);

		mvc.perform(get("/games/findByDeveloper/developer1")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value("game1Id"));
	}
	
	@Test
	@DisplayName("Find game by my games")
	@WithMockUser(value = "spring")
	void testFindGameByMyGames() throws Exception{
		List<Game> games = new ArrayList<>();
		games.add(game1);
		
		when(gameService.findByMyGames("dev1Id")).thenReturn(games);
		when(developerService.findByUsername("spring")).thenReturn(developer1);

		mvc.perform(get("/games/findMyGames")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value("game1Id"));
	}
	
	private String objectToJsonStringContent(final Object o) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(o);
		return requestJson;
	}

}
