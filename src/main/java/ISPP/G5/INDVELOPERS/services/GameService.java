package ISPP.G5.INDVELOPERS.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.repositories.GameRepository;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class GameService {

	@Autowired
	private GameRepository gameRepository;

	private DeveloperService developerService;

	public List<Game> findAll() {
		return gameRepository.findAll();
	}

	public String addGame(Game game) {
		Assert.notNull(game);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Developer developer = this.developerService.findByUsername(userDetails.getUsername());
		boolean isPremium = false;

		if (developer.getIsPremium() != null) {
			isPremium = developer.getIsPremium();
		}

		if (isPremium == false && game.getPrice() != 0.0)
			throw new IllegalArgumentException("Only premium developers can sell non-free games");
		if (isPremium == false && (findByMyGames().size() + 1 == 6))
			throw new IllegalArgumentException(
					"Non premium developers only can have a maximun of five games published");
		if(checkGameTitle(game.getTitle()))
			throw new IllegalArgumentException("A game with the same title is already published");

		
		this.gameRepository.save(game);
		return "Added game with title:" + game.getTitle();
	}

	public List<Game> findByTitle(String title) {
		return findAll().stream().filter(g -> g.getTitle().contains(title)).collect(Collectors.toList());
	}

	public List<Game> findByDeveloper(String name) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Developer developer = this.developerService.findByUsername(userDetails.getUsername());
		return gameRepository.findByDeveloper(developer.getId());
	}

	public List<Game> findByMyGames() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Developer developer = this.developerService.findByUsername(userDetails.getUsername());
		return gameRepository.findByMyGames(developer.getId());
	}

	public String updateGame(Game game) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Developer developer = this.developerService.findByUsername(userDetails.getUsername());
		if (!game.getCreator().getId().equals(developer.getId()))
			throw new IllegalArgumentException("Only the creator of the game can edit it");
		if(checkGameTitle(game.getTitle()))
			throw new IllegalArgumentException("A game with the same title is already published");
		this.gameRepository.save(game);
		return "Updated game with title:" + game.getTitle();
	}

	public void deleteGame(String id) {
		Game game = findById(id);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Developer developer = this.developerService.findByUsername(userDetails.getUsername());
		if (!game.getCreator().getId().equals(developer.getId())) {
			throw new IllegalArgumentException("Only the creator of the game can remove it");
		} else {
			this.gameRepository.deleteById(id);
		}
	}

	public Game findById(String id) {
		return this.gameRepository.findById(id).orElse(null);
	}

	public boolean checkGameTitle(String gameTitle) {
		boolean result = false;

		for (Game game : this.gameRepository.findAll()) {
			if (game.getTitle().equals(gameTitle)) {
				result = true;
			}
		}

		return result;
	}
}
