
package ISPP.G5.INDVELOPERS.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
	private OwnedGameRepository	ownedGameRepository;
	@Autowired
	private DeveloperService	developerService;
	@Autowired
	private GameService			gameService;


	public OwnedGame findByDeveloper(final Developer developer) {
		OwnedGame result = ownedGameRepository.findByBuyerId(developer.getId());

		if (result == null)
			result = new OwnedGame(developer, new ArrayList<Game>());

		return result;
	}

	public List<Game> findAllMyOwnedGames(final Developer developer) {
		List<Game> res = new ArrayList<Game>();
		res.addAll(findByDeveloper(developer).getOwnedGames());
		res.sort((g1,g2)->g1.getTitle().compareTo(g2.getTitle()));
		System.out.println(res);
		return res;
	}

	public String buyGameByDeveloperAndGameId(final Developer developer, final String gameId) {
		OwnedGame ownedGame = findByDeveloper(developer);

		Game game = gameService.findById(gameId);

		if (game != null) {
			if (ownedGame.getOwnedGames().contains(game))
				throw new IllegalArgumentException("Game already owned.");
		} else
			throw new IllegalArgumentException("Game is null.");

		ownedGame.getOwnedGames().add(game);
		ownedGameRepository.save(ownedGame);
		return "Buyed game with title: " + game.getTitle();
	}

	public boolean checkGameOwned(final Developer developer, final String gameId) {
		boolean result = false;
		OwnedGame ownedGame = findByDeveloper(developer);

		Game game = gameService.findById(gameId);

		if (game != null) {
			if (ownedGame.getOwnedGames().contains(game))
				result = true;
		} else
			//Result true para redirigir en la compra de un juego
			result = true;

		return result;
	}
}
