package ISPP.G5.INDVELOPERS.controllers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.mockito.ArgumentMatchers.any;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import static org.mockito.Mockito.when;
import ISPP.G5.INDVELOPERS.controllers.PublicationController;
import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Publication;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.repositories.DeveloperRepository;
import ISPP.G5.INDVELOPERS.repositories.PublicationRepository;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.PublicationService;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;

@WebMvcTest(controllers = PublicationController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class PublicationControllerTests {

	@MockBean
	private DeveloperService developerService;

	@MockBean
	private PublicationService publicationService;

	@MockBean
	private PublicationRepository publicationRepository;

	@MockBean
	private DeveloperRepository developerRepository;

	Publication publication1;
	Publication publication2;
	Developer developer1;
	Developer developer2;

	@BeforeEach
	void setup() {
		String imagenB64 = "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";
		Set<UserRole> roles = new HashSet<UserRole>();
		roles.add(UserRole.USER);
		developer1 = new Developer("martaad", "Marta123", "maartaadq@alum.us.es", imagenB64, roles, "I'm developer",
				"technologies", true, null, new ArrayList<Developer>());
		developer2 = new Developer("miguel001", "Miguel1234", "maartadq11@alum.us.es", imagenB64, roles,
				"I'm developer", "technologies", true, null, new ArrayList<Developer>());
		this.developerService.createDeveloper(developer1);
		this.developerService.createDeveloper(developer2);
		publication1 = new Publication("martaad", null, "description of publication", null, developer1);
		publication2 = new Publication("miguel001", null, "description of publication", null, developer2);
		publication1.setId("TEST_PUBLICATION1_ID");
		this.publicationService.addPublication(publication1, developer1);
		this.publicationService.addPublication(publication2, developer2);
	}

	@AfterEach
	void end() {
		developerRepository.deleteAll();
		publicationRepository.deleteAll();
	}

	@Autowired
	private MockMvc mockMvc;

	@DisplayName("Show all publications")
	@WithMockUser(value = "spring")
	@Test
	void testShowPublications() throws Exception {
		List<Publication> publications = new ArrayList<Publication>();
		publications.add(publication1);
		publications.add(publication2);
		when(this.publicationService.findAll()).thenReturn(publications);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/publications/findAll")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(publication1.getId()));
	}

	@DisplayName("Show publication by id")
	@WithMockUser(value = "spring")
	@Test
	void testShowPublication() throws Exception {
		when(this.publicationService.findById("TEST_PUBLICATION1_ID")).thenReturn(publication1);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/publications/findById/" + publication1.getId()))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id").value(publication1.getId()));
	}

	@DisplayName("Show publication by username")
	@WithMockUser(value = "spring")
	@Test
	void testShowPublicationsByName() throws Exception {
		List<Publication> publications = new ArrayList<Publication>();
		publications.add(publication1);
		when(this.publicationService.findByUSername("martaad")).thenReturn(publications);
		mockMvc.perform(get("/publications/findByName"))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());	
	}
	@DisplayName("Create publication")
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormPublicationSuccess() throws Exception {
		Developer d = this.developerService.findByUsername("martaad");
		Publication p = new Publication("martaad", null, "text of the publication 2", null, d);
		String bodyContent = objectToJsonStringContent(p);
		when(this.publicationService.addPublication(any(Publication.class), any(Developer.class))).thenReturn("Successfully added");
		mockMvc.perform(post("/publications/add").contentType(MediaType.APPLICATION_JSON).content(bodyContent)).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
				
	}
	@DisplayName("Delete publication")
	@WithMockUser(value = "spring")
	@Test
	void testProcessDeletePublicationSuccess() throws Exception {
		when(this.publicationService.deletePublication(publication1, developer1)).thenReturn("Delete publication with id: " + publication1.getId());
		this.mockMvc.perform(delete("/publications/delete/TEST_PUBLICATION1_ID")).andExpect(status().is2xxSuccessful());
	}

	private String objectToJsonStringContent(final Object o) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(o);
		return requestJson;
	}
	
//	@Test
//	@DisplayName("Edit publication test")
//	@WithMockUser(value = "spring")
//	void editPublications() throws Exception {
//		when(this.publicationService.findById("TEST_PUBLICATION1_ID")).thenReturn(publication1);
//		when(this.publicationService.updatePublication(any())).thenReturn("publication1");
//		publication1.setUsername("publicationNumber1");
//		
//		mockMvc.perform(put("/publications/edit/" + "TEST_PUBLICATION1_ID")).andExpect(status().isOk());
//	}
	
	@Test
	@DisplayName("Bad Requests")
	@WithMockUser(value = "spring")
	void badRequestsTest() throws Exception {
		
		ObjectMapper om = new ObjectMapper();
		
		
		when(publicationService.findAll()).thenThrow(IllegalArgumentException.class);
		when(publicationService.findById("1")).thenThrow(IllegalArgumentException.class);
		when(publicationService.addPublication(any(Publication.class), any(Developer.class))).thenThrow(IllegalArgumentException.class);
		when(publicationService.deletePublication(any(Publication.class), any(Developer.class))).thenThrow(IllegalArgumentException.class);
		when(publicationService.updatePublication(any(Publication.class))).thenThrow(IllegalArgumentException.class);
		
		
		
		mockMvc.perform(get("/publications/findAll")).andExpect(status().isBadRequest());
		mockMvc.perform(get("/publications/findById/" + "1")).andExpect(status().isBadRequest());
		mockMvc.perform(post("/publications/add/")).andExpect(status().isBadRequest());
		mockMvc.perform(delete("/publications/delete/" + "1")).andExpect(status().isBadRequest());
		mockMvc.perform(put("/publications/edit/" + "1")).andExpect(status().isBadRequest());
		
	}
	
}
		
