
package ISPP.G5.INDVELOPERS.integration;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.Review;
import ISPP.G5.INDVELOPERS.repositories.DeveloperRepository;
import ISPP.G5.INDVELOPERS.repositories.GameRepository;
import ISPP.G5.INDVELOPERS.repositories.ReviewRepository;
import ISPP.G5.INDVELOPERS.services.ReviewService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
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
	void init() {
		developer = developerRepository.findAll().get(0);
		game = gameRepository.findAll().get(0);
		reviewDefault = reviewRepository.findAll().get(0);
	}

	@Test
	@DisplayName("Adding review test")
	public void testingAddReview() {
		//Eliminamos la review por si ya existe
		reviewService.deleteReview(reviewDefault.getId());

		Review review = new Review("Text", 1.5, false, null, null);
		int reviewsCountBefore = reviewService.findAll().size();
		reviewService.addReview(review, game, developer);
		int reviewsCountAfter = reviewService.findAll().size();
		Assertions.assertThat(reviewsCountBefore).isEqualTo(reviewsCountAfter - 1);
	}

	@Test
	@DisplayName("Trying to Add 2 reviews with same Game and Developer test")
	public void testingAddTooMuchReviews() {
		Assertions.assertThatThrownBy(() -> {
			Review review = new Review("Text", 1.5, false, null, null);
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
		Review review = reviewService.findById("WrongID");
		Assertions.assertThat(review).isNull();
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
		//Restore review
		reviewService.addReview(r, game, developer);
	}

}
