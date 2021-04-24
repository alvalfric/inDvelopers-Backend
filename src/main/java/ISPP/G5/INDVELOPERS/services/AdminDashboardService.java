package ISPP.G5.INDVELOPERS.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.models.AdminDashboard;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.OwnedGame;
import ISPP.G5.INDVELOPERS.repositories.DeveloperRepository;
import ISPP.G5.INDVELOPERS.repositories.GameRepository;
import ISPP.G5.INDVELOPERS.repositories.IncidentRepository;
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
	
	@Autowired
	private DeveloperRepository developerRepository;
	
	@Autowired
	private IncidentRepository incidentRepository;
	
	public AdminDashboard show() {
		
		AdminDashboard dashboard = new AdminDashboard();

		dashboard.setTotalGamesCreated(this.gameRepository.findAll().size());
		dashboard.setTotalPublicationsCreated(this.publicationRepository.findAll().size());
		dashboard.setTotalReviewsCreated(this.reviewRepository.findAll().size());
		dashboard.setTotalGamesPurchased(this.gamesPurchased());
		dashboard.setTotalMoneyEarnedByDevelopers(this.moneyEarned());
		dashboard.setTotalDevelopers(this.developerRepository.findAll().size());
		dashboard.setGamesVerified(this.gameRepository.findVerified().size());
		dashboard.setGamesNotVerified(this.gameRepository.findAll().stream().filter(x->!x.getIsNotMalware()).collect(Collectors.toList()).size());
		dashboard.setTotalIncident(this.incidentRepository.findAll().size());
		dashboard.setIncidentsSolved(this.incidentRepository.findAll().stream().filter(x->x.isSolved()).collect(Collectors.toList()).size());
		dashboard.setIncidentsNotSolved(this.incidentRepository.findNotSolved().size());
		dashboard.setTotalPremiumUsers(this.developerRepository.findAll().stream().filter(x->x.getIsPremium()).collect(Collectors.toList()).size());
		dashboard.setTotalNotPremiumUsers(this.developerRepository.findAll().stream().filter(x->!x.getIsPremium()).collect(Collectors.toList()).size());

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
