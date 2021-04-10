package ISPP.G5.INDVELOPERS.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.cloud.CloudStorageService;
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
	@Autowired
	private DeveloperService developerService;
	@Autowired
	private CloudStorageService cloudStorageService;

	
	public List<Game> findAll(){
		return gameRepository.findAll();
	}
	public List<Game> findVerified(){
		return gameRepository.findVerified();
	}
	
	public List<Game> findNotRevised(){
		return gameRepository.findNotRevised();
	}
	
	public String addGame(Game game, Developer developer){
		Assert.notNull(game);
		game.setCreator(developer);
		this.gameRepository.save(game);
		return "Added game with title:"+ game.getTitle();
	}

	public List<Game> findByTitle(String title) {
		return findAll().stream().filter(g -> g.getTitle().contains(title)).collect(Collectors.toList());
	}
	
	public List<Game> findByDeveloper(String developerId){
		return gameRepository.findByDeveloper(developerId);
	}
	
	public List<Game> findByMyGames(String developerId)  {
		return gameRepository.findByMyGames(developerId);
	}
	
	public String updateGame(Game game){
		Assert.notNull(game);
		this.gameRepository.save(game);
		return "Updated game with title:"+ game.getTitle();
	}
	
	public void deleteGame(String id){
		this.cloudStorageService.deleteFile(this.findById(id).getIdCloud());
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