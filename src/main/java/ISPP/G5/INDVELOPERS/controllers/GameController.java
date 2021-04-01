package ISPP.G5.INDVELOPERS.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.GameService;


@CrossOrigin("*")
@RestController
@RequestMapping("/games")
public class GameController {

	@Autowired
	private GameService gameService;

	@Autowired
	private DeveloperService developerService;

	@Autowired
	public GameController(final GameService gameService, final DeveloperService developerService) {
		this.gameService = gameService;
		this.developerService = developerService;
	}

	@GetMapping("/findAll")
	public ResponseEntity<List<Game>> findAll() {
		try {
			return ResponseEntity.ok(gameService.findAll());
		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@PostMapping("/add")
	public ResponseEntity<String> addGame(@RequestBody Game game) throws NotFoundException {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			game.setCreator(developerService.findByUsername(userDetails.getUsername()));
			return ResponseEntity.status(HttpStatus.CREATED).body(gameService.addGame(game));
		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@PutMapping("/edit/{id}")
	public ResponseEntity<String> updateGame(@PathVariable String id, @RequestBody Game game) {
		try {
			Game gameData = gameService.findById(id);
			gameData.setTitle(game.getTitle());
			gameData.setDescription(game.getDescription());
			gameData.setRequirements(game.getRequirements());
			gameData.setPrice(game.getPrice());
			gameData.setIsNotMalware(game.getIsNotMalware());
			gameData.setIdCloud(game.getIdCloud());
			return new ResponseEntity<>(gameService.updateGame(gameData), HttpStatus.OK);
		} catch(IllegalArgumentException e){
			return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<HttpStatus> deleteGameById(@PathVariable("id") String id) throws NotFoundException{
		try {
			gameService.deleteGame(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch(IllegalArgumentException e) {
			return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/findByTitle/{title}")
	public ResponseEntity<List<Game>> getGameByTitle(@PathVariable String title) {
		try {
			return ResponseEntity.ok(gameService.findByTitle(title));
		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/findByDeveloper/{developerUsername}")
	public ResponseEntity<List<Game>> getGameByDeveloper(@PathVariable String developerUsername) {
		try {
			return ResponseEntity.ok(gameService.findByDeveloper(developerUsername));
		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/findMyGames")
	public ResponseEntity<List<Game>> getGameByMyGames() {
		try {
			return ResponseEntity.ok(gameService.findByMyGames());
		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Game> getGameById(@PathVariable String id) {
		try {
			return ResponseEntity.ok(gameService.findById(id));
		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

}