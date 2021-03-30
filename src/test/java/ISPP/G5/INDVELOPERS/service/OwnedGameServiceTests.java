package ISPP.G5.INDVELOPERS.service;

import static org.assertj.core.api.Assertions.assertThat;

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
    protected OwnedGameService ownedGameService;
    
    @Autowired
    protected DeveloperService developerService;
    
    @Autowired
    protected GameService gameService;
    
    private Developer alvaro;
    private Developer dummyDeveloper;
    private Game game1;
    private Game game2;
    			
    
	@BeforeEach
	void init() throws NotFoundException {
		alvaro = developerService.findByUsername("alvaro");
		dummyDeveloper = developerService.findByUsername("dummyDeveloper");
		game1 = gameService.findByTitle("25 caminos oscuros").get(0);
		game2 = gameService.findByTitle("Payaso que salta").get(0);
	}
    
	@Test
	void shouldFindOwnedGames() {
		OwnedGame ownedGameAlvaro = this.ownedGameService.findByDeveloper(alvaro);
		List<Game> games = new ArrayList<Game>();
		games.add(game1);
		assertThat(ownedGameAlvaro.getBuyer()).isEqualTo(alvaro);
		assertThat(ownedGameAlvaro.getOwnedGames()).isEqualTo(new ArrayList<Game>(games));
	}
    
	@Test
	void shouldNotFindOwnedGames() {
		OwnedGame ownedGameDummyDeveloper = this.ownedGameService.findByDeveloper(dummyDeveloper);
		assertThat(ownedGameDummyDeveloper.getBuyer()).isEqualTo(dummyDeveloper);
		assertThat(ownedGameDummyDeveloper.getOwnedGames()).isEqualTo(new ArrayList<Game>());
	}
	
	@Test
	void shouldBuyAGameForUserWithPurchasedGame() throws NotFoundException {
		OwnedGame ownedGameAlvaro = this.ownedGameService.findByDeveloper(alvaro);
		assertThat(ownedGameAlvaro.getBuyer()).isEqualTo(alvaro);
		assertThat(ownedGameAlvaro.getOwnedGames()).isEqualTo(new ArrayList<Game>(List.of(game1)));
				
		assertThat(this.ownedGameService.buyGameByDeveloperAndGameId(alvaro, game2.getId()).equals("Buyed game with title: "+ game2.getTitle()));
		
		ownedGameAlvaro = this.ownedGameService.findByDeveloper(alvaro);
		assertThat(ownedGameAlvaro.getOwnedGames()).isEqualTo(new ArrayList<Game>(List.of(game1,game2)));
	}
	
	@Test
	void shouldBuyAGameForUserWithoutGames() throws NotFoundException {
		OwnedGame ownedGameDummyDeveloper = this.ownedGameService.findByDeveloper(dummyDeveloper);
		assertThat(ownedGameDummyDeveloper.getBuyer()).isEqualTo(dummyDeveloper);
		assertThat(ownedGameDummyDeveloper.getOwnedGames()).isEqualTo(new ArrayList<Game>());
				
		assertThat(this.ownedGameService.buyGameByDeveloperAndGameId(dummyDeveloper, game1.getId()).equals("Buyed game with title: "+ game1.getTitle()));
		
		ownedGameDummyDeveloper = this.ownedGameService.findByDeveloper(dummyDeveloper);
		assertThat(ownedGameDummyDeveloper.getOwnedGames()).isEqualTo(new ArrayList<Game>(List.of(game1)));
	}
	
	@Test
	void shouldNotBuyTwoGamesWithSameTitle() throws NotFoundException {
		OwnedGame ownedGameAlvaro = this.ownedGameService.findByDeveloper(alvaro);
		assertThat(ownedGameAlvaro.getBuyer()).isEqualTo(alvaro);
		assertThat(ownedGameAlvaro.getOwnedGames()).isEqualTo(new ArrayList<Game>(List.of(game1)));
				
		assertThat(this.ownedGameService.buyGameByDeveloperAndGameId(alvaro, game2.getId()).equals("Buyed game with title: "+ game2.getTitle()));
		
		ownedGameAlvaro = this.ownedGameService.findByDeveloper(alvaro);
		assertThat(ownedGameAlvaro.getOwnedGames()).isEqualTo(new ArrayList<Game>(List.of(game1,game2)));
		
		Assertions.assertThrows(IllegalArgumentException.class, () ->{
			this.ownedGameService.buyGameByDeveloperAndGameId(alvaro, game2.getId());
		});	
	}
	
	@Test
	void shouldThrowExceptionNotExceptionFotNotFoundGame() throws NotFoundException {
		OwnedGame ownedGameAlvaro = this.ownedGameService.findByDeveloper(alvaro);
		assertThat(ownedGameAlvaro.getBuyer()).isEqualTo(alvaro);
		assertThat(ownedGameAlvaro.getOwnedGames()).isEqualTo(new ArrayList<Game>(List.of(game1)));
		
		Assertions.assertThrows(NotFoundException.class, () ->{
			this.ownedGameService.buyGameByDeveloperAndGameId(alvaro, "estejuegonoexiste");
		});	
	}
	
	@Test
	void shouldFindListOfOwnedGmes() throws NotFoundException {
		List<Game> games = this.ownedGameService.findAllMyOwnedGames(alvaro);
		assertThat(games).isEqualTo(new ArrayList<Game>(List.of(game1)));
	}

}
