
package ISPP.G5.INDVELOPERS.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.OwnedGame;
import ISPP.G5.INDVELOPERS.repositories.OwnedGameRepository;
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
	private OwnedGameRepository ownedGameRepository;

	@Autowired
	public GameController(final GameService gameService, final DeveloperService developerService,
			final OwnedGameRepository ownedGameRepository) {
		this.gameService = gameService;
		this.developerService = developerService;
		this.ownedGameRepository = ownedGameRepository;
	}

	@GetMapping("/findVerified")
	public ResponseEntity<List<Game>> findVerified() {
		try {
			return ResponseEntity.ok(gameService.findVerified());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/findAll")
	public ResponseEntity<List<Game>> findAll() {
		try {
			return ResponseEntity.ok(gameService.findAll());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@PostMapping("/add")
	public ResponseEntity<String> addGame(@RequestBody final Game game) throws IllegalArgumentException {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Developer developer = developerService.findByUsername(userDetails.getUsername());
			boolean isPremium = false;
			if (developer.getIsPremium() != null)
				isPremium = developer.getIsPremium();
			if (gameService.findAll().stream().anyMatch(g -> g.getTitle().equals(game.getTitle())))
				throw new IllegalArgumentException("There's already a game with that title");
			if (isPremium == false && game.getPrice() != 0.0)
				throw new IllegalArgumentException("Only premium developers can sell non-free games");
			if (isPremium == false && (gameService.findByMyGames(developer.getId()).size() + 1 > 5))
				throw new IllegalArgumentException(
						"Non premium developers only can have a maximum of five games published");
			return ResponseEntity.status(HttpStatus.CREATED).body(gameService.addGame(game, developer));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@PutMapping("/edit/{id}")
	public ResponseEntity<String> updateGame(@PathVariable final String id, @RequestBody final Game game)
			throws NotFoundException {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Developer developer = developerService.findByUsername(userDetails.getUsername());
			Game gameData = gameService.findById(id);
			List<Game> allGames = gameService.findAll();
			allGames.remove(gameData);

			if (allGames.stream().anyMatch(g -> g.getTitle().equals(game.getTitle())))
				throw new IllegalArgumentException("There's alredy a game with that title");
			if (!gameData.getCreator().getId().equals(developer.getId()))
				throw new IllegalArgumentException("Only the creator of the game can edit it");
			gameData.setTitle(game.getTitle());
			gameData.setDescription(game.getDescription());
			gameData.setRequirements(game.getRequirements());
			gameData.setPrice(game.getPrice());
			gameData.setIsNotMalware(game.getIsNotMalware());
			gameData.setIdCloud(game.getIdCloud());
			return new ResponseEntity<>(gameService.updateGame(gameData), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<HttpStatus> deleteGameById(@PathVariable("id") final String id) throws NotFoundException {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Developer developer = developerService.findByUsername(userDetails.getUsername());
			Game game = gameService.findById(id);
			if (!game.getCreator().getId().equals(developer.getId()))
				throw new IllegalArgumentException("Only the creator of the game can remove it");
			gameService.deleteGame(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/findByTitle/{title}")
	public ResponseEntity<List<Game>> getGameByTitle(@PathVariable final String title) {
		try {
			return ResponseEntity.ok(gameService.findByTitle(title));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/findByDeveloper/{developerUsername}")
	public ResponseEntity<List<Game>> getGameByDeveloper(@PathVariable final String developerUsername)
			throws NotFoundException {
		try {
			Developer developer = developerService.findByUsername(developerUsername);
			return ResponseEntity.ok(gameService.findByDeveloper(developer.getId()));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/findMyGames")
	public ResponseEntity<List<Game>> getGameByMyGames() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Developer developer = developerService.findByUsername(userDetails.getUsername());
			return ResponseEntity.ok(gameService.findByMyGames(developer.getId()));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Game> getGameById(@PathVariable final String id) throws NotFoundException {
		try {
			return ResponseEntity.ok(gameService.findById(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/findByNew")
	public ResponseEntity<List<Game>> findByNew() {
		try {
			List<Game> allGames = this.gameService.findAll();
			Collections.reverse(allGames);
			return ResponseEntity
					.ok(allGames.stream().filter(g -> g.getIsNotMalware() == true).collect(Collectors.toList()));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/findByTopSellers")
	public ResponseEntity<List<Game>> findByTopSellers() {
		Integer actual = 0;
		Integer res = 0;
		Boolean first = true;
		List<Game> topSellersGames = new ArrayList<Game>();
		List<OwnedGame> allOwnedGames = this.ownedGameRepository.findAll();
		List<Game> allGames = this.gameService.findAll();
		Integer size = allGames.size();
		for (int i = 0; i < size; i++) {
			actual = 0;
			first = true;
			for (Game g : allGames) {
				res = 0;

				if (!topSellersGames.contains(g)) {
					for (OwnedGame o: allOwnedGames) {
						if (o.getOwnedGames() != null && o.getOwnedGames().contains(g)) {
							res += 1;
						}
						if (res >= actual) {
							if (first) {
							topSellersGames.add(i, g);
							first = false;
							} else {
								topSellersGames.remove(i);
								topSellersGames.add(i, g);
							}
							actual = res;	
						}
					}
				}
			}
		}
		try {

			return ResponseEntity.ok(topSellersGames);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

}
