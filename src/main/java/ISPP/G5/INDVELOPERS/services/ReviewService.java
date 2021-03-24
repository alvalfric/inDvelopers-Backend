
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
		return this.repository.findAll();
	}

	public Review findById(final String id) {
		return this.repository.findById(id).get();
	}

	public String saveReview(final Review review) {
		this.repository.save(review);
		return "Added Review with Id: " + review.getId();
	}

	public String deleteReview(final String id) {
		this.repository.deleteById(id);
		return "Deleted Review with Id: " + id;
	}

}
