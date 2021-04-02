package ISPP.G5.INDVELOPERS.services;

import java.util.List;
import java.util.Optional;

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
	
	@Autowired
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
		if(developer.getIsPremium() == false && game.getPrice() != 0.0) 
			throw new IllegalArgumentException("Only premium developers can sell non-free games");
		if(developer.getIsPremium() == false && (findByMyGames().size() + 1 == 6))
			throw new IllegalArgumentException("Non premium developers only can have a maximun of five games published");
		if(game.getIsNotMalware()==false)
			throw new IllegalArgumentException("We don´t publish malwares, only games");
		
		this.gameRepository.save(game);
		return "Added game with title:"+ game.getTitle();
	}

	public Game findByTitle(String title) throws NotFoundException {
		return gameRepository.findByTitle(title).orElseThrow(NotFoundException::new);
	}
	
	public List<Game> findByDeveloper(String name) throws NotFoundException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Developer developer = this.developerService.findByUsername(userDetails.getUsername());
		return gameRepository.findByDeveloper(developer.getId());
	}
	
	public List<Game> findByMyGames() throws NotFoundException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Developer developer = this.developerService.findByUsername(userDetails.getUsername());
		return gameRepository.findByMyGames(developer.getId());
	}
	
	public String updateGame(Game game) throws NotFoundException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Developer developer = this.developerService.findByUsername(userDetails.getUsername());
		if (!game.getCreator().getId().equals(developer.getId())) 
			throw new IllegalArgumentException("Only the creator of the game can edit it");
		
		this.gameRepository.save(game);
		return "Updated game with title:"+ game.getTitle();
	}
	
	public void deleteGame(String id) throws NotFoundException {
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
	
	public Game findById(String id) throws NotFoundException {
		return this.gameRepository.findById(id).orElseThrow(NotFoundException::new);
	}
}
