package ISPP.G5.INDVELOPERS.services;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.models.AdminDashboard;
import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.OwnedGame;
import ISPP.G5.INDVELOPERS.models.UserRole;
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
	private DeveloperService developerService;
	
	@Autowired
	private IncidentRepository incidentRepository;
	
	public AdminDashboard show() throws IllegalAccessException {
		if (!this.developerService.findCurrentDeveloper().getRoles().contains(UserRole.ADMIN)) {
			throw new IllegalAccessException("You are not and admin");
		}
		
		AdminDashboard dashboard = new AdminDashboard();
		
		Integer totalGamesCreated = this.gameRepository.findAll() == null ? 0 : this.gameRepository.findAll().size();
		Integer totalPublicationsCreated = this.publicationRepository.findAll() == null ? 0 : this.publicationRepository.findAll().size();
		Integer totalReviewsCreated = this.reviewRepository.findAll() == null ? 0 : this.reviewRepository.findAll().size();
		Integer totalDevelopers = this.developerRepository.findAll() == null ? 0 : this.developerRepository.findAll().size();
		Integer totalGamesVerified = this.gameRepository.findVerified() == null ? 0 : this.gameRepository.findVerified().size();
		Integer totalGamesNonVerified=this.gameRepository.findAll().stream().filter(x->!x.getIsNotMalware()).collect(Collectors.toList())==null?0:this.gameRepository.findAll().stream().filter(x->!x.getIsNotMalware()).collect(Collectors.toList()).size();
		Integer totalIncidents = this.incidentRepository.findAll() == null ? 0 : this.incidentRepository.findAll().size();
		Integer totalIncidentsSolved=this.incidentRepository.findAll().stream().filter(x->x.isSolved()).collect(Collectors.toList())==null?0:this.incidentRepository.findAll().stream().filter(x->x.isSolved()).collect(Collectors.toList()).size();
		Integer totalIncidentsNotSolved = this.incidentRepository.findNotSolved() == null ? 0 : this.incidentRepository.findNotSolved().size();
		Integer totalPremiumUsers;
		Integer totalNonPremiumUsers;
		try {
			totalPremiumUsers=this.developerRepository.findAll().stream().filter(x->x.getIsPremium()).collect(Collectors.toList())==null?0:this.developerRepository.findAll().stream().filter(x->x.getIsPremium()).collect(Collectors.toList()).size();
			totalNonPremiumUsers=this.developerRepository.findAll().stream().filter(x->!x.getIsPremium()).collect(Collectors.toList())==null?0:this.developerRepository.findAll().stream().filter(x->!x.getIsPremium()).collect(Collectors.toList()).size();
		}catch(NullPointerException e) {
			totalPremiumUsers=0;
			totalNonPremiumUsers=0;
		}
		
		dashboard.setTotalGamesCreated(totalGamesCreated);
		dashboard.setTotalPublicationsCreated(totalPublicationsCreated);
		dashboard.setTotalReviewsCreated(totalReviewsCreated);
		dashboard.setTotalGamesPurchased(this.gamesPurchased());
		dashboard.setTotalMoneyEarnedByDevelopers(this.moneyEarned());
		dashboard.setTotalDevelopers(totalDevelopers);
		dashboard.setGamesVerified(totalGamesVerified);
		dashboard.setGamesNotVerified(totalGamesNonVerified);
		dashboard.setTotalIncident(totalIncidents);
		dashboard.setIncidentsSolved(totalIncidentsSolved);
		dashboard.setIncidentsNotSolved(totalIncidentsNotSolved);
		dashboard.setTotalPremiumUsers(totalPremiumUsers);
		dashboard.setTotalNotPremiumUsers(totalNonPremiumUsers);

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
		
		try {
		for(OwnedGame og: ownedGames) {
			for(Game g: og.getOwnedGames()) {
				moneyEarned += g.getPrice();
			}
		}
		}catch(NullPointerException e) {
			moneyEarned=0.0;
		}
		
		return moneyEarned;
	}
}
