package ISPP.G5.INDVELOPERS.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.models.AdminDashboard;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.OwnedGame;
import ISPP.G5.INDVELOPERS.repositories.GameRepository;
import ISPP.G5.INDVELOPERS.repositories.OwnedGameRepository;
import ISPP.G5.INDVELOPERS.repositories.PublicationRepository;
import ISPP.G5.INDVELOPERS.repositories.ReviewRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AdminDashboardService {
	
	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private PublicationRepository publicationRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private OwnedGameRepository ownedGameRepository;
	
	
	public AdminDashboard show() {
		
		AdminDashboard dashboard = new AdminDashboard();

		dashboard.setTotalGamesCreated(this.gameRepository.findAll().size());
		dashboard.setTotalPublicationsCreated(this.publicationRepository.findAll().size());
		dashboard.setTotalReviewsCreated(this.reviewRepository.findAll().size());
		dashboard.setTotalGamesPurchased(this.gamesPurchased());
		dashboard.setTotalMoneyEarnedByDevelopers(this.moneyEarned());
		
		return dashboard;
	}
	
	private Integer gamesPurchased() {
		List<OwnedGame> ownedGames = this.ownedGameRepository.findAll();
		Integer purchasedGames = 0;
		
		for (int i = 0; i < ownedGames.size(); i++) {
			purchasedGames += ownedGames.get(i).getOwnedGames().size();
		}
		
		return purchasedGames;
	}

	private Double moneyEarned() {
		List<OwnedGame> ownedGames = this.ownedGameRepository.findAll();
		Double moneyEarned = 0.0;
		
		for(OwnedGame og: ownedGames) {
			for(Game g: og.getOwnedGames()) {
				moneyEarned += g.getPrice();
			}
		}
		
		return moneyEarned;
	}
}
