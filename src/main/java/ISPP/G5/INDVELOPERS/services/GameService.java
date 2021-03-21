package ISPP.G5.INDVELOPERS.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.repositories.GameRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class GameService {
	
	private GameRepository gameRepository;
	
	public List<Game> findAll(){
		return gameRepository.findAll();
	}
	
	public String addGame(Game game) {
		this.gameRepository.save(game);
		return "Added game with title:"+ game.getTitle();
	}

	public Game findByTitle(String title) throws NotFoundException {
		return gameRepository.findByTitle(title).orElseThrow(NotFoundException::new);
	}
	
	public List<Game> findByDeveloper(Developer developer) {
		return gameRepository.findByDeveloper(developer);
	}
	
	public List<Game> findByMyGames(Developer developer) {
		return gameRepository.findByMyGames(developer);
	}
	
	public Game updateGame(Game gameToUpdate) {
		return this.gameRepository.save(gameToUpdate);
	}
	
	public String deleteGame(Game game) {
		String title = game.getTitle();
		this.gameRepository.delete(game);
		return "The game: " + title + " has been removed succesfully";
	}
	
	public Game findById(String id) throws NotFoundException{
		return this.gameRepository.findById(id).orElseThrow(NotFoundException::new);
	}
}
