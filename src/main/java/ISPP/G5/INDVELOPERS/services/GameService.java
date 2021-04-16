package ISPP.G5.INDVELOPERS.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
//	@Autowired
//	private CloudStorageService cloudStorageService;

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