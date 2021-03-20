package ISPP.G5.INDVELOPERS.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.UserEntity;
import ISPP.G5.INDVELOPERS.services.GameService;


@CrossOrigin("*")
@RestController
@RequestMapping("/Game")
public class GameController {

	@Autowired
	private GameService service;
	
	@Autowired
	public GameController(final GameService gameService) {
		this.service = gameService;
	}
	
	@GetMapping("/findAll")
	public List<Game> findAll() {
		return service.findAll();
	}
	
	@PostMapping("/add")
	public String addPrueba(@RequestBody Game game) {
		return service.addGame(game);
	}
	
	@GetMapping("/{title}")
	public ResponseEntity<Game> getGameByTitle(@PathVariable String title) {
		try {
			return ResponseEntity.ok(this.service.findByTitle(title));
		} catch(IllegalArgumentException | NotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	@GetMapping("/{title}/delete")
	@RequestMapping(value = "{title}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteGameByTitle(@PathVariable String title) throws NotFoundException {
		Game game = this.service.findByTitle(title);
		if(game == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} 
		
		this.service.deletesGame(game);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}