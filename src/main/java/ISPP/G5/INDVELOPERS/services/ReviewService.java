
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
		if (!repository.findById(id).isPresent())
			return null;

		return repository.findById(id).get();
	}

	public String addReview(final Review review) {
		// VALIDADOR para evitar que undeveloper pueda realizar varias reviews al mismo juego
		/*
		 * List<Review> allReviews = repository.findAll();
		 * for(Review re: allReviews) {
		 * if(re.getDeveloper().getId().equals(review.getDeveloper().getId()) &&
		 * re.getGame().getId().equals(review.getGame().getId()))
		 * return "Not saved Review because Developer with Id: " + review.getDeveloper().getId()+
		 * " already had reviewed the Game with Id: " + review.getDeveloper().getId();
		 * }
		 */
		repository.save(review);
		return "Added Review with Id: " + review.getId();
	}

	public String updateReview(final Review review) {
		if (!repository.findById(review.getId()).isPresent())
			return "Error Id: Review with id " + review.getId() + " do not exist";

		repository.save(review);
		return "Updated Review with Id: " + review.getId();
	}

	public String deleteReview(final String id) {
		if (!repository.findById(id).isPresent())
			return "Error Id: Review with id " + id + " do not exist";

		repository.deleteById(id);
		return "Deleted Review with Id: " + id;
	}

}
