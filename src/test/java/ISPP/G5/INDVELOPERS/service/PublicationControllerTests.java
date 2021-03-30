package ISPP.G5.INDVELOPERS.service;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.DeleteMapping;

import ISPP.G5.INDVELOPERS.controllers.PublicationController;
import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Publication;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.repositories.DeveloperRepository;
import ISPP.G5.INDVELOPERS.repositories.PublicationRepository;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.PublicationService;

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
	
	@BeforeEach
	void setup() {
		Set<UserRole> roles = new HashSet<UserRole>();
		roles.add(UserRole.USER);
		Developer developer1 = new Developer("martaad", "Marta123", "maartaadq@alum.us.es", null, null, roles,
				"I'm developer", "technologies", true);
		this.developerService.createDeveloper(developer1);
		Publication publication = new Publication("martaad", null, "description of publication", null, developer1);
		this.publicationService.addPublication(publication, developer1);
	}
	
	@AfterEach
	void end() {
		developerRepository.deleteAll();
		publicationRepository.deleteAll();
	}

	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(value = "spring")
	@Test
	void testShowPublications() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/publications/findAll"))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowPublication() throws Exception {
		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/publications/findById/{id}"))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}
	
	@WithMockUser(value = "spring", authorities = { "developer" })
	@Test
	void testShowPublicationsByName() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/publications/findByName"))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormPublicationSuccess() throws Exception {
		this.mockMvc.perform(post("/publications/add").with(csrf())
				.param("username", "martaad")
				.param("text", "text of the publication"))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}
	
//	@WithMockUser(value = "spring")
//	@Test
//	void testProcessDeletePublicationSuccess() throws Exception {
//		this.mockMvc.perform(DeleteMapping("/publications/add").with(csrf())
//				.param("username", "martaad")
//				.param("text", "text of the publication"))
//				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()));
//	}

	
}
