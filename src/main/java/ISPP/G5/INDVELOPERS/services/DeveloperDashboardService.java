package ISPP.G5.INDVELOPERS.services;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.DeveloperDashboard;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.OwnedGame;
import ISPP.G5.INDVELOPERS.models.Publication;
import ISPP.G5.INDVELOPERS.models.Review;
import ISPP.G5.INDVELOPERS.repositories.GameRepository;
import ISPP.G5.INDVELOPERS.repositories.OwnedGameRepository;
import ISPP.G5.INDVELOPERS.repositories.PublicationRepository;
import ISPP.G5.INDVELOPERS.repositories.ReviewRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DeveloperDashboardService {
	
	
	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private PublicationRepository publicationRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private OwnedGameRepository ownedGameRepository;
	
	@Autowired
	private DeveloperService developerService;

	
	public DeveloperDashboard show() {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Developer developer = this.developerService.findByUsername(userDetails.getUsername());

			DeveloperDashboard dashboard = new DeveloperDashboard();
			
			dashboard.setNumGamesCreated(this.findGameByDeveloper(developer.getId()).size());
			dashboard.setNumReviewsCreated(this.findReviewByDeveloper(developer.getId()).size());
			dashboard.setNumPublicationsCreated(this.findPublicationByUSername(developer.getId()).size());
			dashboard.setNumGamesOwned(this.findOwnedGames(developer));
			dashboard.setMoneyEarned(this.moneyEarned(developer));
			
			return dashboard;
		}
	
	public List<Game> findGameByDeveloper(String developerId){
		return gameRepository.findByDeveloper(developerId);
	}
	
	public List<Publication> findPublicationByUSername(String username) {
		List<Publication> allPublications = this.publicationRepository.findAll();
		List<Publication> publications = new ArrayList<Publication>();
		for (Publication p : allPublications) {
			if (p.getUsername().contentEquals(username)) {
				publications.add(p);
			}
		}
		return publications;
	}
	
	public List<Review> findReviewByDeveloper(String developerId){
		return reviewRepository.findByMyReviews(developerId);
	}
	
	public List<Game> findGameByMyGames(String developerId)  {
		return gameRepository.findByMyGames(developerId);
	}

	public Integer findOwnedGames(Developer developer) {
		OwnedGame ownedGame = this.ownedGameRepository.findByBuyerId(developer.getId());

		if(ownedGame == null) {
			return 0;
		} else {
			return ownedGame.getOwnedGames().size();
		}
	}
	
	public Double moneyEarned(Developer developer) {
		Double result = 0.0;
		List<Game> gamesCreated = this.findGameByMyGames(developer.getId());
		List<OwnedGame> ownedGames = this.ownedGameRepository.findAll();
		
		if(ownedGames.isEmpty()) {
			return result;
		} 
		
		for(OwnedGame og: ownedGames) {
			for(Game g: og.getOwnedGames()) {
				for(Game gameCreated: gamesCreated) {
					if(g.getId().equals(gameCreated.getId())) {
						result = result + g.getPrice();
					}
				}
			}
		}
		
		return result;
	}
	
}
