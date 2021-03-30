
package ISPP.G5.INDVELOPERS.unitTests.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ISPP.G5.INDVELOPERS.controllers.ReviewController;
import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.Review;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.GameService;
import ISPP.G5.INDVELOPERS.services.ReviewService;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerUnitTests {

	@Autowired
	private MockMvc		mvc;

	@InjectMocks
	ReviewController	controller;

	@Mock
	ReviewService		service;

	@Mock
	DeveloperService	devService;

	@Mock
	GameService			gameService;

	Developer			developer;
	Game				game;
	Review				r1;
	Review				r2;


	@BeforeEach
	void setUp() {
		developer = new Developer("hola", "adios", "email", new ArrayList<String>(), null, new HashSet<UserRole>(), "description", "none", true);
		developer.setId("devID");
		game = new Game("title", "description", "requirements", 2., "iddClou", true, developer);
		game.setId("gameID");
		r1 = new Review("Text1", 1., game, developer);
		r1.setId("rev1ID");
		r2 = new Review("Text2", 2., game, developer);
		r2.setId("rev2ID");
	}

	@Test
	@DisplayName("Show reviews by game id test")
	void showReviewListByGameTest() throws Exception {
		when(service.findAllByGameId("ID")).thenReturn(Lists.list(r1, r2));

		mvc.perform(get("/reviews/game/ID")).andExpect(status().isOk()).andExpect(content().json("[]"));
	}

	
	/*
	@Test
	@DisplayName("Show review by id test")
	void showReviewByIdTest() throws Exception {
		when(service.findById("ID")).thenReturn(r1);

		mvc.perform(get("/reviews/ID")).andExpect(status().isOk()).andExpect(content().json("{}"));
	}

	@Test
	@DisplayName("Create review for a game test")
	@WithMockUser(value = "spring")
	void createReviewForAGame() throws Exception {
		Review rev = new Review("Text1", 1., null, null);

		when(service.addReview(rev, game, developer)).thenReturn("Added Review");
		when(gameService.findById("ID")).thenReturn(game);
		when(devService.findByUsername("spring")).thenReturn(developer);

		mvc.perform(post("/reviews/game/ID/add", rev)).andExpect(status().isOk()).andExpect(content().string("Added Review"));
	}

	@Test
	@DisplayName("Edit review test")
	@WithMockUser(value = "spring")
	void editReview() throws Exception {
		Review rev = new Review("Text1", 1., null, null);

		when(service.updateReview(rev)).thenReturn("Edited Review");
		when(devService.findByUsername("spring")).thenReturn(developer);

		mvc.perform(put("/reviews/edit/ID", rev)).andExpect(status().isOk()).andExpect(content().string("Edited Review"));
	}

	@Test
	@DisplayName("Delete review test")
	@WithMockUser(value = "spring")
	void deleteReview() throws Exception {

		when(service.deleteReview("ID")).thenReturn("Deleted Review");
		when(service.findById("ID")).thenReturn(r1);
		when(devService.findByUsername("spring")).thenReturn(developer);

		mvc.perform(delete("/reviews/delete/ID")).andExpect(status().isOk()).andExpect(content().string("Deleted Review"));
	}*/
}
