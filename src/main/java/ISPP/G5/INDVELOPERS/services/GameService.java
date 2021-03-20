package ISPP.G5.INDVELOPERS.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

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
		gameRepository.save(game);
		return "Added game with Id:"+ game.getId();
	}

	public Game findByTitle(String title) throws NotFoundException {
		return gameRepository.findByTitle(title).orElseThrow(NotFoundException::new);
	}
	
	public Game updateGame(Game game) {
		return this.gameRepository.save(game);
	}
	
	public void deletesGame(Game game) {
		this.gameRepository.delete(game);
	}
	
	public Game findById(String id) throws NotFoundException{
		return this.gameRepository.findById(id).orElseThrow(NotFoundException::new);
	}
}
