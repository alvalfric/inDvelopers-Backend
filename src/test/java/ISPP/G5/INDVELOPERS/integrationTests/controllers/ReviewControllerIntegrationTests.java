
package ISPP.G5.INDVELOPERS.integrationTests.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import ISPP.G5.INDVELOPERS.models.Review;
import ISPP.G5.INDVELOPERS.repositories.ReviewRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerIntegrationTests {

	@Autowired
	private MockMvc		mvc;

	@Autowired
	ReviewRepository	repository;


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
	@WithMockUser(value = "spring")
	void createReviewForAGame() throws Exception {
		Review rev = new Review("Text1", 1., null, null);
		String bodyContent = objectToJsonStringContent(rev);
		Review r = repository.findAll().get(0);
		String id = r.getGame().getId();

		mvc.perform(post("/reviews/game/" + id + "/add").contentType(MediaType.APPLICATION_JSON).content(bodyContent)).andExpect(status().isOk()).andExpect(content().string("Added Review"));
	}

	@Test
	@DisplayName("Edit review test")
	@WithMockUser(value = "spring")
	void editReview() throws Exception {
		Review rev = new Review("Text1", 1., null, null);
		String bodyContent = objectToJsonStringContent(rev);
		Review r = repository.findAll().get(0);
		String id = r.getGame().getId();

		mvc.perform(put("/reviews/edit/" + id).contentType(MediaType.APPLICATION_JSON).content(bodyContent)).andExpect(status().isOk()).andExpect(content().string("Edited Review"));
	}

	@Test
	@DisplayName("Delete review test")
	@WithMockUser(value = "spring")
	void deleteReview() throws Exception {
		Review r = repository.findAll().get(0);
		String id = r.getGame().getId();

		mvc.perform(delete("/reviews/delete/" + id)).andExpect(status().isOk()).andExpect(content().string("Deleted Review"));
	}

	private String objectToJsonStringContent(final Object o) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(o);
		return requestJson;
	}
}
