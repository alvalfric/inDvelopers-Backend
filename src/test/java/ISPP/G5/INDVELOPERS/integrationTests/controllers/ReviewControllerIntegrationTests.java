
package ISPP.G5.INDVELOPERS.integrationTests.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.Review;
import ISPP.G5.INDVELOPERS.repositories.DeveloperRepository;
import ISPP.G5.INDVELOPERS.repositories.GameRepository;
import ISPP.G5.INDVELOPERS.repositories.ReviewRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerIntegrationTests {

	@Autowired
	private MockMvc					mvc;

	@Autowired
	protected GameRepository		gameRepository;

	@Autowired
	protected DeveloperRepository	developerRepository;

	@Autowired
	protected ReviewRepository		reviewRepository;

	Developer						developer;
	Game							game;
	Review							r1;


	@BeforeEach
	void setUp() {
		developer = developerRepository.findAll().get(0);
		game = gameRepository.findAll().get(0);
		r1 = reviewRepository.findAll().get(0);
	}

	@Test
	@DisplayName("Show reviews by game id test")
	void showReviewListByGameTest() throws Exception {

		mvc.perform(get("/reviews/game/" + r1.getGame().getId())).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(r1.getId()));
	}

	@Test
	@DisplayName("Show review by id test")
	void showReviewByIdTest() throws Exception {
		mvc.perform(get("/reviews/" + r1.getId())).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(r1.getId()));
	}

	@Test
	@DisplayName("Create review for a game test")
	@WithMockUser(value = "master")
	void createReviewForAGame() throws Exception {
		Review rev = new Review("Text1", 1., null, null);

		mvc.perform(post("/reviews/game/" + game.getId() + "/add", rev)).andExpect(status().isOk()).andExpect(content().string("Added Review"));
	}

	@Test
	@DisplayName("Edit review test")
	@WithMockUser(value = "spring")
	void editReview() throws Exception {
		Review rev = new Review("Text1", 1., null, null);

		mvc.perform(put("/reviews/edit/ID", rev)).andExpect(status().isOk()).andExpect(content().string("Edited Review"));
	}

	@Test

	@DisplayName("Delete review test")

	@WithMockUser(value = "spring")
	void deleteReview() throws Exception {
		mvc.perform(delete("/reviews/delete/ID")).andExpect(status().isOk()).andExpect(content().string("Deleted Review"));
	}
}
