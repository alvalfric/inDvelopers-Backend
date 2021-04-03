package ISPP.G5.INDVELOPERS.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.OwnedGame;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.GameService;
import ISPP.G5.INDVELOPERS.services.OwnedGameService;

@CrossOrigin("*")
@RestController
@RequestMapping("/ownedGames")
public class OwnedGameController {

	@Autowired
	private OwnedGameService ownedGameService;
	@Autowired
	private DeveloperService developerService;
	@Autowired
	private GameService gameService;

	@Autowired
	public OwnedGameController(final DeveloperService developerService, final GameService gameService,
		final OwnedGameService ownedGameService) {
		this.developerService = developerService;
		this.ownedGameService = ownedGameService;
		this.gameService = gameService;
	}

	@GetMapping("/findOwnedGames")
	public ResponseEntity<List<Game>> findAll() {
		try {
			List<Game> ownedGames = ownedGameService
				.findAllMyOwnedGames(developerService.findCurrentDeveloper());

			if (ownedGames == null) {
				ownedGames = new ArrayList<Game>();
			}

			return ResponseEntity.ok(ownedGames);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@PostMapping("/buy/{gameId}")
	public ResponseEntity<String> buyGame(@PathVariable String gameId) {
		try {
			Developer developer = developerService.findCurrentDeveloper();
			OwnedGame ownedGame = ownedGameService.findByDeveloper(developer);
			Game game = gameService.findById(gameId);

			if (game != null) {
				if (ownedGame.getOwnedGames().contains(game))
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			} else
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

			return ResponseEntity.status(HttpStatus.CREATED)
				.body(ownedGameService.buyGameByDeveloperAndGameId(developer, gameId));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/checkGameOwned/{gameId}")
	public ResponseEntity<Boolean> checkGameOwned(@PathVariable String gameId) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(
				ownedGameService.checkGameOwned(developerService.findCurrentDeveloper(), gameId));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

}