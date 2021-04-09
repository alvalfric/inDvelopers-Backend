package ISPP.G5.INDVELOPERS.services;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.models.AdminDashboard;
import ISPP.G5.INDVELOPERS.models.DeveloperDashboard;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.Publication;
import ISPP.G5.INDVELOPERS.models.Review;
import ISPP.G5.INDVELOPERS.repositories.GameRepository;
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
	
	
		
	
   public DeveloperDashboard showOne() {
		
	DeveloperDashboard result = new DeveloperDashboard();

		return result;
	}
	
	

}
