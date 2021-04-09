
package ISPP.G5.INDVELOPERS.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

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
	private MockMvc		mvc;

	@Autowired
	ReviewRepository	repository;

	@Autowired
	DeveloperRepository	devRepository;

	@Autowired
	GameRepository		gameRepository;

	Review				reviewDefault;


	@BeforeEach
	void init() {
		Developer dev = devRepository.findByUsername("master2").get();
		Game game = gameRepository.findAll().get(0);
		Review r = new Review("ALgo", 2., game, dev);
		repository.save(r);
		List<Review> revs = repository.findAllByGameId(game.getId());
		for (Review r1 : revs)
			if (r1.getDeveloper().getId().equals(dev.getId()))
				reviewDefault = r1;
	}

	@Test
	@DisplayName("Show reviews by game id test")
	void showReviewListByGameTest() throws Exception {
		Review r = repository.findAll().get(0);
		String id = r.getGame().getId();

		mvc.perform(get("/reviews/game/" + id)).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(r.getId()));
	}

	@Test
	@DisplayName("Show review by id test")
	void showReviewByIdTest() throws Exception {
		Review r = repository.findAll().get(0);
		String id = r.getId();

		mvc.perform(get("/reviews/" + id)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id));
	}

	@Test
	@DisplayName("Create review for a game test")
	@WithMockUser(username = "master2")
	void createReviewForAGame() throws Exception {
		repository.deleteAll();
		Review rev = new Review("Text1", 1., null, null);
		String bodyContent = objectToJsonStringContent(rev);

		Game game = gameRepository.findAll().get(0);

		mvc.perform(post("/reviews/game/" + game.getId() + "/add").contentType(MediaType.APPLICATION_JSON).content(bodyContent)).andExpect(status().isOk());
	}

	@Test
	@DisplayName("Edit review test")
	@WithMockUser(username = "master2")
	void editReview() throws Exception {
		Review rev = new Review("Text1", 1., null, null);
		String bodyContent = objectToJsonStringContent(rev);

		mvc.perform(put("/reviews/edit/" + reviewDefault.getId()).contentType(MediaType.APPLICATION_JSON).content(bodyContent)).andExpect(status().isOk()).andExpect(content().string("Updated Review with Id: " + reviewDefault.getId()));
	}

	@Test
	@DisplayName("Delete review test")
	@WithMockUser(username = "master2")
	void deleteReview() throws Exception {

		mvc.perform(delete("/reviews/delete/" + reviewDefault.getId())).andExpect(status().isOk()).andExpect(content().string("Deleted Review with Id: " + reviewDefault.getId()));
	}

	private String objectToJsonStringContent(final Object o) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(o);
		return requestJson;
	}
}
