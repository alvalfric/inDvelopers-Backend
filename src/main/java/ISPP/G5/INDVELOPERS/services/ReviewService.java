
package ISPP.G5.INDVELOPERS.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.Review;
import ISPP.G5.INDVELOPERS.repositories.ReviewRepository;
import io.jsonwebtoken.lang.Assert;

@Service
public class ReviewService {

	@Autowired
	private ReviewRepository repository;


	public List<Review> findAll() {
		return repository.findAll();
	}

	public List<Review> findAllByGameId(final String gameId) {
		return repository.findAllByGameId(gameId);
	}

	public Review findById(final String id) {
		return repository.findById(id).orElse(null);
	}

	public String addReview(final Review review, final Game game, final Developer developer) {
		Assert.notNull(review);
		review.setDeveloper(developer);
		review.setGame(game);
		List<Review> allReviews = findAllByGameId(game.getId());
		for (Review re : allReviews)
			if (re.getDeveloper().getId().equals(developer.getId()))
				throw new IllegalArgumentException("Not saved Review because Developer with Id: " + review.getDeveloper().getId() + " already had reviewed the Game with Id: " + review.getGame().getId());

		repository.save(review);
		return "Added Review with Id: " + review.getId();
	}

	public String updateReview(final Review review) {
		if (!repository.findById(review.getId()).isPresent())
			throw new IllegalArgumentException("Error Id: Review with id " + review.getId() + " do not exist");

		repository.save(review);
		return "Updated Review with Id: " + review.getId();
	}

	public String deleteReview(final String id) {
		if (!repository.findById(id).isPresent())
			throw new IllegalArgumentException("Error Id: Review with id " + id + " do not exist");

		repository.deleteById(id);
		return "Deleted Review with Id: " + id;
	}

}
