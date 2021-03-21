package ISPP.G5.INDVELOPERS.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
			return ResponseEntity.ok(this.gameService.findAll());
		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	@PostMapping("/add")
	public ResponseEntity<String> addGame(@RequestBody Game game) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(this.gameService.addGame(game));

		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	@PutMapping("/edit/{id}")
	public ResponseEntity<String> updateGame(@PathVariable String id, @RequestBody Game game) throws NotFoundException{
		try {
			this.gameService.updateGame(game);
			return new ResponseEntity<String>("Game update succesfully", HttpStatus.OK);
		} catch(IllegalArgumentException e){
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteGameById(@PathVariable String id) throws NotFoundException{
		Game game = this.gameService.findById(id);
		try {
			this.gameService.deleteGame(game);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch(IllegalArgumentException e) {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/find/{title}")
	public ResponseEntity<Game> getGameByTitle(@PathVariable String title) {
		try {
			return ResponseEntity.ok(this.gameService.findByTitle(title));
		} catch(IllegalArgumentException | NotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	@GetMapping("/find/{developerUsername}")
	public ResponseEntity<List<Game>> getGameByDeveloper(@PathVariable String developerUsername) {
		try {
			return ResponseEntity.ok(this.gameService.findByDeveloper(this.developerService.findByUsername(developerUsername)));
		} catch(IllegalArgumentException | NotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	@GetMapping("/find/myGames")
	public ResponseEntity<List<Game>> getGameByMyGames() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String name = authentication.getName();
			return ResponseEntity.ok(this.gameService.findByMyGames(this.developerService.findByUsername(name)));
		} catch(IllegalArgumentException | NotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
}