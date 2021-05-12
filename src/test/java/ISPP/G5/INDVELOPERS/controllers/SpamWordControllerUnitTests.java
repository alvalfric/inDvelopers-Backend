
package ISPP.G5.INDVELOPERS.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ISPP.G5.INDVELOPERS.dtos.GetDeveloperDTO;
import ISPP.G5.INDVELOPERS.mappers.DeveloperDTOConverter;
import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.Publication;
import ISPP.G5.INDVELOPERS.models.Review;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.services.SpamWordService;

@SpringBootTest
@AutoConfigureMockMvc
public class SpamWordControllerUnitTests {

	@Autowired
	private MockMvc			mockMvc;

	@MockBean
	private SpamWordService	service;

	Game					game		= new Game();
	Developer				developer	= new Developer("developer1", "password", "email1@gmail.com", null, new HashSet<UserRole>(), null, null, null, null, new ArrayList<Developer>());
	Review					review		= new Review(null, 0, null, game, developer);
	Publication				publication	= new Publication(null, null, null, null, null);


	@Test
	@DisplayName("Analyze Game")
	@WithMockUser(value = "spring")
	void analyzeGameTest() throws Exception {
		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(game);

		when(service.CheckGame(any(Game.class))).thenReturn(true);
		mockMvc.perform(post("/spam/game").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
	}

	@Test
	@DisplayName("Analyze Review")
	@WithMockUser(value = "spring")
	void analyzeReviewTest() throws Exception {
		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(review);

		when(service.CheckReview(any(Review.class))).thenReturn(true);
		mockMvc.perform(post("/spam/review").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
	}

	@Test
	@DisplayName("Analyze Publication")
	@WithMockUser(value = "spring")
	void analyzePublicationTest() throws Exception {
		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(publication);

		when(service.CheckPublication(any(Publication.class))).thenReturn(true);
		mockMvc.perform(post("/spam/publication").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
	}

	@Test
	@DisplayName("Analyze Developer")
	@WithMockUser(value = "spring")
	void analyzeDeveloperTest() throws Exception {
		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(developer);

		when(service.CheckDeveloper(any(Developer.class))).thenReturn(true);
		mockMvc.perform(post("/spam/signupDeveloper").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
	}

	@Test
	@DisplayName("Analyze Developer DTO")
	@WithMockUser(value = "spring")
	void analyzeGetDeveloperDTOTest() throws Exception {
		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(DeveloperDTOConverter.DevelopertoGetDeveloperDTO(developer));

		when(service.CheckGetDeveloperDTO(any(GetDeveloperDTO.class))).thenReturn(true);
		mockMvc.perform(post("/spam/editDeveloper").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
	}

	@Test
	@DisplayName("Bad Requests")
	@WithMockUser(value = "spring")
	void badRequestsTest() throws Exception {
		ObjectMapper om = new ObjectMapper();

		when(service.CheckGame(any(Game.class))).thenThrow(IllegalArgumentException.class);
		when(service.CheckReview(any(Review.class))).thenThrow(IllegalArgumentException.class);
		when(service.CheckPublication(any(Publication.class))).thenThrow(IllegalArgumentException.class);
		when(service.CheckDeveloper(any(Developer.class))).thenThrow(IllegalArgumentException.class);
		when(service.CheckGetDeveloperDTO(any(GetDeveloperDTO.class))).thenThrow(IllegalArgumentException.class);

		String devJson = om.writeValueAsString(developer);
		String devDTOJson = om.writeValueAsString(DeveloperDTOConverter.DevelopertoGetDeveloperDTO(developer));
		String pubJson = om.writeValueAsString(publication);
		String revJson = om.writeValueAsString(review);
		String gameJson = om.writeValueAsString(game);

		mockMvc.perform(post("/spam/game").contentType(MediaType.APPLICATION_JSON).content(gameJson)).andExpect(status().isBadRequest());
		mockMvc.perform(post("/spam/review").contentType(MediaType.APPLICATION_JSON).content(revJson)).andExpect(status().isBadRequest());
		mockMvc.perform(post("/spam/publication").contentType(MediaType.APPLICATION_JSON).content(pubJson)).andExpect(status().isBadRequest());
		mockMvc.perform(post("/spam/signupDeveloper").contentType(MediaType.APPLICATION_JSON).content(devJson)).andExpect(status().isBadRequest());
		mockMvc.perform(post("/spam/editDeveloper").contentType(MediaType.APPLICATION_JSON).content(devDTOJson)).andExpect(status().isBadRequest());

	}

}
