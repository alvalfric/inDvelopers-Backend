
package ISPP.G5.INDVELOPERS.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
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

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Incident;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.IncidentService;

@SpringBootTest
@AutoConfigureMockMvc
class IncidentControllerTests {

	@Autowired
	private MockMvc				mockMvc;

	@MockBean
	private IncidentService		incidentService;

	@MockBean
	private DeveloperService	developerService;

	Developer					developer1;
	Incident					incident1;
	Incident					incident2;


	@BeforeEach
	void init() throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		Set<UserRole> setRole = new HashSet<UserRole>();
		setRole.add(UserRole.USER);
		setRole.add(UserRole.ADMIN);

		developer1 = new Developer("developer1", "password", "email1@gmail.com", null, setRole, null, null, null, formatter.parse("1999-05-05"), new ArrayList<Developer>());
		developer1.setId("id1");
		incident1 = new Incident("I can't buy a game", "The system won't let me buy a game", "Payment error", LocalDate.of(2021, 4, 23), null, false, developer1);
		incident1.setId("333");
		incident2 = new Incident("I can't buy a membership", "The system won't let me buy a membership", "No button", LocalDate.of(2021, 4, 24), null, false, developer1);
		incident2.setId("334");

	}

	@Test
	@DisplayName("Incident list")
	@WithMockUser(value = "spring")
	void showIncidentListTest() throws Exception {

		when(incidentService.findAll()).thenReturn(Lists.list(incident1, incident2));
		when(developerService.findByUsername(any(String.class))).thenReturn(developer1);
		when(this.developerService.checkDeveloperIsAdmin(developer1)).thenReturn(true);
		mockMvc.perform(get("/incidents")).andExpect(status().isOk()).andExpect(jsonPath("$[0].title").value(incident1.getTitle())).andExpect(jsonPath("$[1].title").value(incident2.getTitle()));
	}

	@Test
	@DisplayName("Get incident by ID")
	@WithMockUser(value = "spring")
	void showIncidentbyId() throws Exception {

		when(incidentService.findById("333")).thenReturn(incident1);
		when(developerService.findByUsername(any(String.class))).thenReturn(developer1);
		when(this.developerService.checkDeveloperIsAdmin(developer1)).thenReturn(true);

		mockMvc.perform(get("/incidents/" + "333")).andExpect(status().isOk()).andExpect(jsonPath("$.cause").value(incident1.getCause()));
	}

	@Test
	@DisplayName("Create incident")
	@WithMockUser(value = "spring")
	void createIncident() throws Exception {
		when(developerService.findByUsername(any(String.class))).thenReturn(developer1);
		when(incidentService.addIncident(any(Incident.class), any(Developer.class))).thenReturn("Correct");

		Incident incident = new Incident("I can't buy a membership", "Don't let me buy a membership", "No button", null, null, false, null);
		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(incident);
		mockMvc.perform(post("/incidents/add").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated());
	}
}
