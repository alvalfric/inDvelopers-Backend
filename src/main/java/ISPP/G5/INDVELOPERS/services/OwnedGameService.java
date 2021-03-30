package ISPP.G5.INDVELOPERS.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.OwnedGame;
import ISPP.G5.INDVELOPERS.repositories.OwnedGameRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OwnedGameService {

	@Autowired
	private OwnedGameRepository ownedGameRepository;
	@Autowired
	private DeveloperService developerService;
	@Autowired
	private GameService gameService;

	public OwnedGame findByDeveloper(Developer developer) {
		OwnedGame result = ownedGameRepository.findByBuyerId(developer.getId());

		if (result == null) {
			result = new OwnedGame(developer, new ArrayList<Game>());
		}

		return result;
	}

	public List<Game> findAllMyOwnedGames(Developer developer) throws NotFoundException {
		return new ArrayList<>(this.findByDeveloper(developer).getOwnedGames());
	}

	public String buyGameByDeveloperAndGameId(Developer developer, String gameId) throws NotFoundException {
		OwnedGame ownedGame = this.findByDeveloper(developer);

		Game game = this.gameService.findById(gameId);

		if (game != null) {
			if (ownedGame.getOwnedGames().contains(game)) {
				throw new IllegalArgumentException("Game already owned.");
			}
		} else {
			throw new NotFoundException();
		}
		
		ownedGame.getOwnedGames().add(game);
		this.ownedGameRepository.save(ownedGame);
		return "Buyed game with title: " + game.getTitle();
	}
}
