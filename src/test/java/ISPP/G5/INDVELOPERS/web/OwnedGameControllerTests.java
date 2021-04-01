package ISPP.G5.INDVELOPERS.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ISPP.G5.INDVELOPERS.controllers.OwnedGameController;
import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.OwnedGame;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.GameService;
import ISPP.G5.INDVELOPERS.services.OwnedGameService;

@WebMvcTest(controllers = OwnedGameController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class))
class OwnedGameControllerTests {


	@Autowired
	private OwnedGameController ownedGameController;

	@MockBean
	private DeveloperService developerService;

	@MockBean
	private GameService gameService;
	
	@MockBean
	private OwnedGameService ownedGameService;
	
	@Autowired
	private MockMvc mockMvc;
		
	private Developer dummyDeveloperWithoutGames;
	private Developer dummyDeveloperWithCreatedGames;
	private Game dummyGame1;
	
	@BeforeEach
	void setup() throws NotFoundException {
		dummyDeveloperWithoutGames = PopulateTestData.dummyDeveloper("idDummyDeveloperWithoutGames");
		dummyDeveloperWithoutGames = PopulateTestData.dummyDeveloper("idDummyDeveloperWithCreatedGames");
		dummyGame1 = PopulateTestData.dummyGame(dummyDeveloperWithoutGames, "idGame1");
		
		given(this.developerService.findCurrentDeveloper()).willReturn(dummyDeveloperWithoutGames);
	}
	
    @WithMockUser(value = "spring")
	    @Test
	void testFindOwnedGamesWithoutGames() throws Exception {
		given(this.ownedGameService.findAllMyOwnedGames(dummyDeveloperWithoutGames)).willReturn(null);
	    
		mockMvc.perform(get("/ownedGames/findOwnedGames")
	            .with(csrf())
	            )
		        .andExpect(status().isOk());
    }
    
    @WithMockUser(value = "spring")
	    @Test
	void testFindOwnedGamesWithGames() throws Exception {
		List<Game> games = new ArrayList<Game>();
		games.add(dummyGame1);
		given(this.ownedGameService.findAllMyOwnedGames(dummyDeveloperWithoutGames)).willReturn(games);
	    
		mockMvc.perform(get("/ownedGames/findOwnedGames")
	            .with(csrf())
	            )
		        .andExpect(status().isOk());
	}
    
    @WithMockUser(value = "spring")
	    @Test
	void testFindOwnedGamesBadRequest() throws Exception {
		List<Game> games = new ArrayList<Game>();
		games.add(dummyGame1);
		given(this.ownedGameService.findAllMyOwnedGames(dummyDeveloperWithoutGames)).willReturn(games);
	    
		mockMvc.perform(post("/ownedGames/findOwnedGames")
	            .with(csrf())
	            )
		        .andExpect(status().is4xxClientError());
	}
    
    @WithMockUser(value = "spring")
	    @Test
	void testBuyGame() throws Exception {
    	given(this.ownedGameService.buyGameByDeveloperAndGameId(dummyDeveloperWithoutGames, dummyGame1.getId())).willReturn("Buyed game with title: "+ dummyGame1.getTitle());
	    given(this.ownedGameService.findByDeveloper(dummyDeveloperWithoutGames)).willReturn(new OwnedGame(dummyDeveloperWithoutGames, new ArrayList<Game>()));
    	given(this.gameService.findById(dummyGame1.getId())).willReturn(dummyGame1);
    	
		mockMvc.perform(post("/ownedGames/buy?gameId={gameId}", dummyGame1.getId())
	            .with(csrf())
	            )
		        .andExpect(status().is2xxSuccessful());
	}
    
    @WithMockUser(value = "spring")
	    @Test
	void testBuyAlreadyOwnedGame() throws Exception {
		List<Game> games = new ArrayList<Game>();
		games.add(dummyGame1);
    	
		given(this.ownedGameService.buyGameByDeveloperAndGameId(dummyDeveloperWithoutGames, dummyGame1.getId())).willReturn("Buyed game with title: "+ dummyGame1.getTitle());
	    given(this.ownedGameService.findByDeveloper(dummyDeveloperWithoutGames)).willReturn(new OwnedGame(dummyDeveloperWithoutGames, games));
		given(this.gameService.findById(dummyGame1.getId())).willReturn(dummyGame1);
		
		mockMvc.perform(post("/ownedGames/buy?gameId={gameId}", dummyGame1.getId())
	            .with(csrf())
	            )
		        .andExpect(status().is4xxClientError());
	}
        
    @WithMockUser(value = "spring")
    	@Test
	void testBuyWrongGame() throws Exception {
		List<Game> games = new ArrayList<Game>();
		games.add(dummyGame1);
		
		given(this.ownedGameService.buyGameByDeveloperAndGameId(dummyDeveloperWithoutGames, dummyGame1.getId())).willReturn("Buyed game with title: "+ dummyGame1.getTitle());
	    given(this.ownedGameService.findByDeveloper(dummyDeveloperWithoutGames)).willReturn(new OwnedGame(dummyDeveloperWithoutGames, games));
		given(this.gameService.findById(dummyGame1.getId())).willReturn(null);
		
		mockMvc.perform(post("/ownedGames/buy?gameId={gameId}", dummyGame1.getId())
	            .with(csrf())
	            )
		        .andExpect(status().is4xxClientError());
	}
    
    @WithMockUser(value = "spring")
	    @Test
	void testBuyGameBadRequest() throws Exception {
		given(this.ownedGameService.buyGameByDeveloperAndGameId(dummyDeveloperWithoutGames, dummyGame1.getId())).willReturn("Buyed game with title: "+ dummyGame1.getTitle());
	    given(this.ownedGameService.findByDeveloper(dummyDeveloperWithoutGames)).willReturn(new OwnedGame(dummyDeveloperWithoutGames, new ArrayList<Game>()));
		given(this.gameService.findById(dummyGame1.getId())).willReturn(dummyGame1);
		
		mockMvc.perform(post("/ownedGames/buy")
	            .with(csrf())
	            )
		        .andExpect(status().is4xxClientError());
	}
    
    @WithMockUser(value = "spring")
	    @Test
	void testCheckGameOwnedTrue() throws Exception {
		given(this.ownedGameService.checkGameOwned(this.developerService.findCurrentDeveloper(), dummyGame1.getId())).willReturn(true);
	    given(this.ownedGameService.findByDeveloper(dummyDeveloperWithoutGames)).willReturn(new OwnedGame(dummyDeveloperWithoutGames, new ArrayList<Game>()));
		given(this.gameService.findById(dummyGame1.getId())).willReturn(dummyGame1);
		
		mockMvc.perform(get("/ownedGames/checkGameOwned?gameId={gameId}", dummyGame1.getId())
	            .with(csrf())
	            )
		        .andExpect(status().is2xxSuccessful());
	}
    
    @WithMockUser(value = "spring")
	    @Test
	void testCheckGameOwnedFalse() throws Exception {
		given(this.ownedGameService.checkGameOwned(this.developerService.findCurrentDeveloper(), dummyGame1.getId())).willReturn(false);
	    given(this.ownedGameService.findByDeveloper(dummyDeveloperWithoutGames)).willReturn(new OwnedGame(dummyDeveloperWithoutGames, new ArrayList<Game>()));
		given(this.gameService.findById(dummyGame1.getId())).willReturn(dummyGame1);
		
		mockMvc.perform(get("/ownedGames/checkGameOwned?gameId={gameId}", dummyGame1.getId())
	            .with(csrf())
	            )
		        .andExpect(status().is2xxSuccessful());
	}
    
    @WithMockUser(value = "spring")
	    @Test
	void testCheckGameOwnedBadRequest() throws Exception {
		given(this.ownedGameService.checkGameOwned(this.developerService.findCurrentDeveloper(), "badRequestId")).willReturn(true);
	    given(this.ownedGameService.findByDeveloper(dummyDeveloperWithoutGames)).willReturn(new OwnedGame(dummyDeveloperWithoutGames, new ArrayList<Game>()));
		given(this.gameService.findById(dummyGame1.getId())).willReturn(dummyGame1);
		
		mockMvc.perform(get("/ownedGames/checkGameOwned")
	            .with(csrf())
	            )
		        .andExpect(status().is4xxClientError());
	}
}
