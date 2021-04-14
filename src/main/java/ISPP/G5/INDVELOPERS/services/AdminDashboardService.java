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
	
	
	public AdminDashboard showOne() {
		
		AdminDashboard result = new AdminDashboard();

		result.setNumGamesDone(this.gameRepository.findAll().size());
		result.setNumPublications(this.publicationRepository.findAll().size());
		result.setNumReviews(this.reviewRepository.findAll().size());
		
		//Todas las entidades ownedGames
		
		List<OwnedGame> todos = this.ownedGameRepository.findAll();
		
		//Iteramos para sacar el numero de juegos
		
		Integer juegosTotales = 0;
		
		for (int i = 0; i < todos.size(); i++) {
			juegosTotales += todos.get(i).getOwnedGames().size();
		}
		
		
		result.setNumGamesOwned(juegosTotales);

		return result;
	}

}
