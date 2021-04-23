package ISPP.G5.INDVELOPERS.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.OwnedGame;
import ISPP.G5.INDVELOPERS.repositories.GameRepository;
import ISPP.G5.INDVELOPERS.repositories.OwnedGameRepository;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class GameService {

	@Autowired
	private GameRepository gameRepository;
	@Autowired
	private OwnedGameRepository ownedGameRepository;

	public List<Game> findAll() {
		List<Game> res = new ArrayList<>();
		res = gameRepository.findAll();
		Collections.reverse(res);
		return res;
	}
	
	public List<Game> findVerified() {
		List<Game> res = new ArrayList<>();
		res = gameRepository.findVerified();
		Collections.reverse(res);
		return res;
	}

	public List<Game> findNotRevised() {
		List<Game> res = new ArrayList<>();
		res = gameRepository.findNotRevised();
		Collections.reverse(res);
		return res;
	}

	public String addGame(Game game, Developer developer) {
		Assert.notNull(game);
		game.setCreator(developer);
		Date fechaCreacion = new Date();
		game.setFechaCreacion(fechaCreacion);
		this.gameRepository.save(game);
		return "Added game with title:" + game.getTitle();
	}

	public List<Game> findByTitle(String title) {
		List<Game> res = new ArrayList<>();
		res = findAll().stream().filter(g -> g.getTitle().contains(title)).collect(Collectors.toList());
		Collections.reverse(res);
		return res;
	}

	public List<Game> findByDeveloper(String developerId) {
		List<Game> res = new ArrayList<>();
		res = gameRepository.findByDeveloper(developerId);
		Collections.reverse(res);
		return res;
	}

	public List<Game> findByMyGames(String developerId) {
		List<Game> res = new ArrayList<>();
		res = gameRepository.findByMyGames(developerId);
		Collections.reverse(res);
		return res;

	}
	
	public List<Game> findByTopSellers() {
		Integer actual = 0;
		Integer res = 0;
		Boolean first = true;
		List<Game> topSellersGames = new ArrayList<Game>();
		List<OwnedGame> allOwnedGames = this.ownedGameRepository.findAll();
		List<Game> allGames = gameRepository.findVerified();
		Integer size = allGames.size();
		for (int i = 0; i < size; i++) {
			actual = 0;
			first = true;
			for (Game g : allGames) {
				res = 0;

				if (!topSellersGames.contains(g)) {
					for (OwnedGame o : allOwnedGames) {
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
		return topSellersGames;

	}

	public String updateGame(Game game) {
		Assert.notNull(game);
		this.gameRepository.save(game);
		return "Updated game with title:" + game.getTitle();
	}
	
	public void deleteGame(String id){
//		this.cloudStorageService.deleteFile(this.findById(id).getIdCloud());
		this.gameRepository.deleteById(id);
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
