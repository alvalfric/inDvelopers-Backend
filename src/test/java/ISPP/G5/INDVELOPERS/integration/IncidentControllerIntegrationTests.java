
package ISPP.G5.INDVELOPERS.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Incident;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.repositories.DeveloperRepository;
import ISPP.G5.INDVELOPERS.repositories.IncidentRepository;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class IncidentControllerIntegrationTests {

	@Autowired
	private MockMvc		mockMvc;

	@Autowired
	DeveloperRepository	drepo;
	
	@Autowired
	IncidentRepository irepo;

	Developer			developer1;
	Developer			developer2;
	Set<UserRole>		setRole1;
	Incident incident1, incident2;


	@BeforeEach
	void init() {
		setRole1 = new HashSet<UserRole>();
		setRole1.add(UserRole.USER);
		developer1 = new Developer("developer1", "password", "email1@gmail.com", null, setRole1, null, null, null, null, new ArrayList<Developer>());
		drepo.save(developer1);
		incident1 = new Incident("Si", "Que si", "Se", LocalDate.of(2021, 4, 24), null, false, developer1);
		incident1.setId("333");
		irepo.save(incident1);
		incident2 = new Incident("Si2", "Que si2", "Se2", LocalDate.of(2021, 4, 25), null, false, developer1);
	}

	@Test
	@DisplayName("Show incident list")
	@WithMockUser(username = "master2")
	void showIncidentListTest() throws Exception {
		mockMvc.perform(get("/incidents")).andExpect(status().isOk()).andExpect(jsonPath("$[0].title").value("Si"));
		drepo.delete(developer1);
		irepo.delete(incident1);
	}

	@Test
	@WithMockUser(username = "master2", authorities = {
		"USER", "ADMIN"
	})
	@DisplayName("Get incident by id")
	void showIncidentbyId() throws Exception {

		mockMvc.perform(get("/incidents/" + "333")).andExpect(status().isOk()).andExpect(jsonPath("$.title").value(incident1.getTitle()));
		drepo.delete(developer1);
		irepo.delete(incident1);

	}


	@Test
	@WithMockUser(username = "alvaro", authorities = {
			"USER"
		})
	@DisplayName("Create incident")
	void createIncident() throws Exception {
		Incident incident = new Incident("I can't buy a membership", "Don't let me buy a membership", "No button", null, null, false, null);
		
		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(incident);
		mockMvc.perform(post("/incidents/add").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated());

	}

	@Test
	@WithMockUser(username = "master2", authorities = {
		"USER", "ADMIN"
	})
	@DisplayName("Solve incident")
	void SolveIncident() throws Exception {

		mockMvc.perform(get("/incidents/solve" + "333")).andExpect(status().isOk());
		drepo.delete(developer1);
		irepo.delete(incident1);
	}

	
	@Test
	@WithMockUser(username = "master2", authorities = {
		"USER", "ADMIN"
	})
	@DisplayName("Delete incident test")
	void deleteIncident() throws Exception {
		String id = irepo.findById("333").get().getId();
		mockMvc.perform(delete("/incidents/delete/" + id)).andExpect(status().isOk());
	}
}
