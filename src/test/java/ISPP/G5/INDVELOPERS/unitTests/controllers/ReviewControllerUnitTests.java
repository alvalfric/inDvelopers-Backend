//
//package ISPP.G5.INDVELOPERS.unitTests.controllers;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.ObjectWriter;
//import com.fasterxml.jackson.databind.SerializationFeature;
//
//import ISPP.G5.INDVELOPERS.models.Developer;
//import ISPP.G5.INDVELOPERS.models.Game;
//import ISPP.G5.INDVELOPERS.models.Review;
//import ISPP.G5.INDVELOPERS.models.UserRole;
//import ISPP.G5.INDVELOPERS.services.DeveloperService;
//import ISPP.G5.INDVELOPERS.services.GameService;
//import ISPP.G5.INDVELOPERS.services.ReviewService;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class ReviewControllerUnitTests {
//
//	@Autowired
//	private MockMvc		mvc;
//
//	@MockBean
//	ReviewService		service;
//
//	@MockBean
//	DeveloperService	devService;
//
//	@MockBean
//	GameService			gameService;
//
//	Developer			developer;
//	Game				game;
////	Review				r1;
//	Review				r2;
//
//
//	@BeforeEach
//	void setUp() {
//		developer = new Developer("hola", "adios", "email", new ArrayList<String>(), null, new HashSet<UserRole>(), "description", "none", true);
//		developer.setId("devID");
//		game = new Game("title", "description", "requirements", 2., "iddClou", true, developer);
//		game.setId("gameID");
//		r1 = new Review("Text1", 1., game, developer);
//		r1.setId("rev1ID");
//		r2 = new Review("Text2", 2., game, developer);
//		r2.setId("rev2ID");
//	}
//
//	@Test
//	@DisplayName("Show reviews by game id test")
//	void showReviewListByGameTest() throws Exception {
//		List<Review> revs = new ArrayList<>();
//		revs.add(r1);
//		revs.add(r2);
//		when(service.findAllByGameId("ID")).thenReturn(revs);
//
//		mvc.perform(get("/reviews/game/ID")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value("rev1ID"));
//	}
//
//	@Test
//	@DisplayName("Show review by id test")
//	void showReviewByIdTest() throws Exception {
//		when(service.findById("ID")).thenReturn(r1);
//
//		mvc.perform(get("/reviews/ID")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value("rev1ID"));
//	}
//
//	@Test
//	@DisplayName("Create review for a game test")
//	@WithMockUser(value = "spring")
//	void createReviewForAGame() throws Exception {
//		Review rev = new Review("Text1", 1., null, null);
//		String bodyContent = objectToJsonStringContent(rev);
//
//		when(service.addReview(any(Review.class), any(Game.class), any(Developer.class))).thenReturn("Added Review");
//		when(gameService.findById("ID")).thenReturn(game);
//		when(devService.findByUsername("spring")).thenReturn(developer);
//
//		mvc.perform(post("/reviews/game/ID/add").contentType(MediaType.APPLICATION_JSON).content(bodyContent)).andExpect(status().isOk()).andExpect(content().string("Added Review"));
//	}
//
//	@Test
//	@DisplayName("Edit review test")
//	@WithMockUser(value = "spring")
//	void editReview() throws Exception {
//		String bodyContent = objectToJsonStringContent(r1);
//
//		when(service.updateReview(any(Review.class))).thenReturn("Edited Review");
//		when(service.findById("ID")).thenReturn(r1);
//		when(devService.findByUsername("spring")).thenReturn(developer);
//
//		mvc.perform(put("/reviews/edit/ID").contentType(MediaType.APPLICATION_JSON).content(bodyContent)).andExpect(status().isOk()).andExpect(content().string("Edited Review"));
//	}
//
//	@Test
//	@DisplayName("Delete review test")
//	@WithMockUser(value = "spring")
//	void deleteReview() throws Exception {
//
//		when(service.deleteReview("ID")).thenReturn("Deleted Review");
//		when(service.findById("ID")).thenReturn(r1);
//		when(devService.findByUsername("spring")).thenReturn(developer);
//
//		mvc.perform(delete("/reviews/delete/ID")).andExpect(status().isOk()).andExpect(content().string("Deleted Review"));
//	}
//
//	private String objectToJsonStringContent(final Object o) throws JsonProcessingException {
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
//		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
//		String requestJson = ow.writeValueAsString(o);
//		return requestJson;
//	}
//}
