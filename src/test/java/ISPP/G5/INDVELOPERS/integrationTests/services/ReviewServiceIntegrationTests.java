
package ISPP.G5.INDVELOPERS.integrationTests.services;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.Review;
import ISPP.G5.INDVELOPERS.repositories.DeveloperRepository;
import ISPP.G5.INDVELOPERS.repositories.GameRepository;
import ISPP.G5.INDVELOPERS.repositories.ReviewRepository;
import ISPP.G5.INDVELOPERS.services.ReviewService;

@SpringBootTest
public class ReviewServiceIntegrationTests {

	@Autowired
	protected ReviewRepository		reviewRepository;

	@Autowired
	protected ReviewService			reviewService;

	@Autowired
	protected GameRepository		gameRepository;

	@Autowired
	protected DeveloperRepository	developerRepository;

	private Game					game;
	private Developer				developer;

	private Review					reviewDefault;


	@BeforeEach
	void init() throws NotFoundException {
		developer = developerRepository.findAll().get(0);
		game = gameRepository.findAll().get(0);
		Review review = new Review("Text", 1.5, game, developer);
		reviewRepository.save(review);
		reviewDefault = reviewRepository.findAll().get(0);
	}

	@AfterEach
	void clear() {
		reviewRepository.deleteAll();
	}

	@Test
	@DisplayName("Adding review test")
	public void testingAddReview() throws NotFoundException {
		//Eliminate review to prevent Exception
		reviewRepository.deleteAll();
		//Adding review
		Review review = new Review("Text", 1.5, null, null);
		int reviewsCountBefore = reviewService.findAll().size();
		reviewService.addReview(review, game, developer);
		int reviewsCountAfter = reviewService.findAll().size();
		Assertions.assertThat(reviewsCountBefore).isEqualTo(reviewsCountAfter - 1);
	}

	@Test
	@DisplayName("Trying to Add 2 reviews with same Game and Developer test")
	public void testingAddTooMuchReviews() throws NotFoundException {
		Assertions.assertThatThrownBy(() -> {
			Review review = new Review("Text", 1.5, null, null);
			reviewService.addReview(review, game, developer);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("Finding all reviews test")
	public void testingFindAll() {
		List<Review> allReviews = reviewService.findAll();
		Assertions.assertThat(allReviews.isEmpty()).isFalse();
		//Assertions.assertThat(allReviews.size()).isEqualTo(1);
	}

	@Test
	@DisplayName("Finding all reviews by game id test")
	public void testingFindAllByGameId() {
		List<Review> allReviews = reviewService.findAllByGameId(game.getId());
		Assertions.assertThat(allReviews.isEmpty()).isFalse();
		//Assertions.assertThat(allReviews.size()).isEqualTo(2);
	}

	@Test
	@DisplayName("Finding review by id test")
	public void testingFindById() throws NotFoundException {
		reviewDefault = reviewService.findAll().get(0);
		Review review = reviewService.findById(reviewDefault.getId());
		Assertions.assertThat(review).isNotNull();
		Assertions.assertThat(review.getId()).isEqualTo(reviewDefault.getId());
	}

	@Test
	@DisplayName("Finding review by wrong id test")
	public void testingFindByWrongIdReturnNull() throws NotFoundException {
		String wrongId = "WrongID";
		Assertions.assertThatThrownBy(() -> {
			reviewService.findById(wrongId);
		}).isInstanceOf(NotFoundException.class);
	}

	@Test
	@DisplayName("Updating review test")
	public void testingUpdateReview() throws NotFoundException {
		Review r = reviewService.findAll().get(0);
		String textBefore = r.getText();
		int reviewsCountBefore = reviewService.findAll().size();
		r.setText("Texto Camiado");
		reviewService.updateReview(r);
		int reviewsCountAfter = reviewService.findAll().size();
		String textAfter = reviewService.findAll().get(0).getText();
		Assertions.assertThat(reviewsCountBefore).isEqualTo(reviewsCountAfter);
		Assertions.assertThat(textBefore).isNotEqualTo(textAfter);
	}

	@Test
	@DisplayName("Deleting review test")
	public void testingDeleteReview() throws NotFoundException {
		Review r = reviewService.findAll().get(0);
		int reviewsCountBefore = reviewService.findAll().size();
		reviewService.deleteReview(r.getId());
		int reviewsCountAfter = reviewService.findAll().size();
		Assertions.assertThat(reviewsCountBefore).isEqualTo(reviewsCountAfter + 1);
	}

}
