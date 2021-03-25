
package ISPP.G5.INDVELOPERS.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.models.Review;
import ISPP.G5.INDVELOPERS.repositories.ReviewRepository;

@Service
public class ReviewService {

	@Autowired
	private ReviewRepository repository;


	public String communicateWithReact() {
		return "Mensaje desde Spring";
	}

	public List<Review> findAll() {
		return repository.findAll();
	}

	public Review findById(final String id) {
		return repository.findById(id).get();
	}

	public String saveReview(final Review review) {
		List<Review> allReviews = repository.findAll();
		for(Review re: allReviews) {
			if(re.getDeveloper().getId().equals(review.getDeveloper().getId()) &&
				re.getGame().getId().equals(review.getGame().getId()))
				return "Not saved Review because Developer with Id: " + review.getDeveloper().getId()+
					" already had reviewed the Game with Id: " + review.getDeveloper().getId();
		}
		repository.save(review);
		return "Saved Review with Id: " + review.getId();
	}

	public String deleteReview(final String id) {
		repository.deleteById(id);
		return "Deleted Review with Id: " + id;
	}

}
