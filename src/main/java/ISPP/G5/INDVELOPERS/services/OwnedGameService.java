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
	
	public OwnedGame findByBuyerId(String buyerId) {
		return ownedGameRepository.findByBuyerId(buyerId);
	}
	
	public OwnedGame findByCurrentUser() throws NotFoundException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Developer developer = this.developerService.findByUsername(userDetails.getUsername());
		return ownedGameRepository.findByBuyerId(developer.getId());
	}
	
	public List<Game> findAllMyOwnedGames() throws NotFoundException {
		return new ArrayList<>(this.findByCurrentUser().getOwnedGame());
	}
	
	public String buyGameByGameId(String gameId) throws NotFoundException {
		OwnedGame ownedGame = this.findByCurrentUser();
		
		if(ownedGame == null) {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Developer developer = this.developerService.findByUsername(userDetails.getUsername());
			ownedGame = new OwnedGame(developer, new ArrayList<Game>());
		}
		
		Game game = this.gameService.findById(gameId);
		
		if(ownedGame.getOwnedGame().contains(game)) {
			throw new IllegalArgumentException("Game already owned.");
		}
		
		ownedGame.getOwnedGame().add(game);
		this.ownedGameRepository.save(ownedGame);
		return "Buyed game with title: "+ game.getTitle();
	}
}
