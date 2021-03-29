package ISPP.G5.INDVELOPERS.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.OwnedGame;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.GameService;
import ISPP.G5.INDVELOPERS.services.OwnedGameService;

@SpringBootTest
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
		assertThat(ownedGameAlvaro.getBuyer()).isEqualTo(alvaro);
		assertThat(ownedGameAlvaro.getOwnedGame()).isEqualTo(new ArrayList<Game>(List.of(game1)));
	}
    
}
