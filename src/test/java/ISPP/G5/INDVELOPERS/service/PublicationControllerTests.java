package ISPP.G5.INDVELOPERS.service;

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
		Set<UserRole> roles = new HashSet<UserRole>();
		roles.add(UserRole.USER);
		developer1 = new Developer("martaad", "Marta123", "maartaadq@alum.us.es", null, null, roles, "I'm developer",
				"technologies", true);
		developer2 = new Developer("miguel001", "Miguel1234", "maartadq11@alum.us.es", null, null, roles,
				"I'm developer", "technologies", true);
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
		this.mockMvc.perform(delete("/publications/delete/TEST_PUBLICATION1_ID")).andExpect(status().isOk());
	}

	private String objectToJsonStringContent(final Object o) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(o);
		return requestJson;
	}
}
