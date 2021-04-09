package ISPP.G5.INDVELOPERS.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.models.AdminDashboard;
import ISPP.G5.INDVELOPERS.repositories.GameRepository;
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
	
	
	public AdminDashboard showOne() {
		
		AdminDashboard result = new AdminDashboard();

		result.setNumGamesDone(this.gameRepository.findAll().size());
		result.setNumPublications(this.publicationRepository.findAll().size());
		result.setNumReviews(this.reviewRepository.findAll().size());
		result.setNumGamesOwned(0);

		return result;
	}

}
