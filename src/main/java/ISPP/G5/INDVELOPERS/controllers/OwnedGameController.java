package ISPP.G5.INDVELOPERS.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ISPP.G5.INDVELOPERS.models.Game;
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
	private GameService gameService;
	
	@Autowired
	private DeveloperService developerService;
	
	@Autowired
	public OwnedGameController(final OwnedGameService ownedGameService, final GameService gameService, final DeveloperService developerService) {
		this.ownedGameService = ownedGameService;
		this.gameService = gameService;
		this.developerService = developerService;
	}
	
	@GetMapping("/findOwnedGames")
	public ResponseEntity<List<Game>> findAll() throws NotFoundException {
		try {
			return ResponseEntity.ok(new ArrayList<>(this.ownedGameService.findByCurrentUser().getOwnedGame()));
		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	@PostMapping("/buy")
	public ResponseEntity<String> buyGame(@RequestParam String gameId) throws NotFoundException {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(this.ownedGameService.buyGameByGameId(gameId));
		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
}