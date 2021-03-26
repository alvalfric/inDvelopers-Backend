package ISPP.G5.INDVELOPERS.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
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
	
	private GameRepository gameRepository;
	
	private DeveloperService developerService;
	
	
	public List<Game> findAll(){
		return gameRepository.findAll();
	}
	
	public String addGame(Game game) throws NotFoundException {
		Assert.notNull(game);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Developer developer = this.developerService.findByUsername(userDetails.getUsername());
		if(developer.getIsPremium() == false && game.getPrice() != 0) 
			throw new IllegalArgumentException("Only premium developers can sell non-free games");
		if(developer.getIsPremium() == false && (developer.getGameList().size() + 1 == 6))
			throw new IllegalArgumentException("Premium developers only can have a maximun of five games published");
		
		this.gameRepository.save(game);
		return "Added game with title:"+ game.getTitle();
	}

	public Game findByTitle(String title) throws NotFoundException {
		return gameRepository.findByTitle(title).orElseThrow(NotFoundException::new);
	}
	
	public List<Game> findByDeveloper(Developer developer) {
		return gameRepository.findByDeveloper(developer);
	}
	
	/*public List<Game> findByMyGames(Developer developer) {
		return gameRepository.findByMyGames(developer);
	}*/
	
	public Game updateGame(Game gameToUpdate) throws NotFoundException {
		Assert.notNull(gameToUpdate);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Developer developer = this.developerService.findByUsername(userDetails.getUsername());
		if (gameToUpdate.getCreator() != developer) 
			throw new IllegalArgumentException("Only the creator of the game can edit it");
		
		return this.gameRepository.save(gameToUpdate);
	}
	
	public String deleteGame(Game game) throws NotFoundException {
		Assert.notNull(game);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Developer developer = this.developerService.findByUsername(userDetails.getUsername());
		if (game.getCreator() != developer) 
			throw new IllegalArgumentException("Only the creator of the game can remove it");
		String title = game.getTitle();
		this.gameRepository.delete(game);
		return "The game: " + title + " has been removed succesfully";
	}
	
	public Game findById(String id) throws NotFoundException{
		return this.gameRepository.findById(id).orElseThrow(NotFoundException::new);
	}
}
