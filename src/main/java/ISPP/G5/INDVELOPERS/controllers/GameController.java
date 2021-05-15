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

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.SearchData;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.DeveloperSubscriptionService;
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
	private DeveloperSubscriptionService developerSubscriptionService;

	@Autowired
	public GameController(final GameService gameService, final DeveloperService developerService,
			DeveloperSubscriptionService developerSubscriptionService) {
		this.gameService = gameService;
		this.developerService = developerService;
		this.developerSubscriptionService = developerSubscriptionService;
	}

	@GetMapping("/findVerified")
	public ResponseEntity<List<Game>> findVerified() {
		try {
			return ResponseEntity.ok(gameService.findVerified());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/findNotRevised")
	public ResponseEntity<List<Game>> findNotRevised() {
		try {
			return ResponseEntity.ok(gameService.findNotRevised());
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
			boolean isPremium = this.developerSubscriptionService.checkDeveloperHasSubscription(developer);
			if (!gameService.findByTitle(game.getTitle()).isEmpty())
				throw new IllegalArgumentException("There's already a game with that title");
			if (isPremium == false && game.getPrice() != 0.0)
				throw new IllegalArgumentException("Only premium developers can sell non-free games");
			if (isPremium == false && gameService.findByMyGames(developer.getId()).size() + 1 > 5)
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
			List<Game> allGamesWithSameName = gameService.findByTitle(game.getTitle());
			allGamesWithSameName.remove(gameData);
			
			if (allGamesWithSameName.stream().anyMatch(g -> g.getTitle().equals(game.getTitle())))
				throw new IllegalArgumentException("There's alredy a game with that title");

			if (gameData.getCreator().getId().equals(developer.getId()) || developer.getRoles().contains(UserRole.ADMIN)) {
				gameData.setTitle(game.getTitle());
				gameData.setDescription(game.getDescription());
				gameData.setRequirements(game.getRequirements());
				gameData.setPrice(game.getPrice());
				gameData.setIsNotMalware(game.getIsNotMalware());
				gameData.setIdCloud(game.getIdCloud());
				gameData.setImagen(game.getImagen());
				gameData.setCategorias(game.getCategorias());
				gameData.setPegi(game.getPegi());
				gameData.setDiscount(game.getDiscount());
				gameData.setGallery(game.getGallery());
				gameData.setUrlVideo(game.getUrlVideo());
				return new ResponseEntity<>(gameService.updateGame(gameData), HttpStatus.OK);
			} else {
				throw new IllegalArgumentException("Only the creator of the game or an admin can update it");
			}

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
			if (game.getCreator().getId().equals(developer.getId()) || developer.getRoles().contains(UserRole.ADMIN)) {
				gameService.deleteGame(id);
			} else {
				throw new IllegalArgumentException("Only the creator of the game or an admin can remove it");
			}
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
	@GetMapping("/findGamesByFollowedDeveloper")
	public ResponseEntity<List<Game>> getGamesByFollowedDeveloper() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Developer developer = developerService.findByUsername(userDetails.getUsername());
			return ResponseEntity.ok(gameService.gamesByDevelopersFollowed(developer));
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
			return ResponseEntity.ok(this.gameService.findVerified());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/findByTopSellers")
	public ResponseEntity<List<Game>> findByTopSellers() {
		try {

			return ResponseEntity.ok(this.gameService.findByTopSellers());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/findByTitleVerifiedOrCategorie/{res}")
	public ResponseEntity<List<Game>> findByTitleVerifiedOrCategorie(@PathVariable final String res) {
		try {
			return ResponseEntity.ok(this.gameService.findByTitleVerifiedOrCategorie(res));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	@GetMapping("/findByTitleVerifiedOrCategorie")
	public ResponseEntity<List<Game>> findByTitleVerifiedOrCategorie2(@RequestBody SearchData res) {
		try {
			System.out.println(res);
			return ResponseEntity.ok(this.gameService.findByTitleVerifiedOrCategorie(res.getQuery()));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/findByPrice/{price}")
	public ResponseEntity<List<Game>> findByPrice(@PathVariable final Double price) {
		try {
			return ResponseEntity.ok(this.gameService.findByPrice(price));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/findByTitleVerified/{title}")
	public ResponseEntity<List<Game>> findGameByTitleVerified(@PathVariable final String title) {
		try {
			return ResponseEntity.ok(gameService.findByTitleVerified(title));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}


	@GetMapping("/findAllWithDiscount")
	public ResponseEntity<List<Game>> findAllWithDiscount() {
		try {
			return ResponseEntity.ok(this.gameService.findAllWithDiscount());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

}
