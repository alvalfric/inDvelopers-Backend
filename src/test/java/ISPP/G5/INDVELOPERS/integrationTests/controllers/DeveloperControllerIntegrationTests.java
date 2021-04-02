package ISPP.G5.INDVELOPERS.integrationTests.controllers;



import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
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

import com.google.gson.Gson;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.repositories.DeveloperRepository;
import ISPP.G5.INDVELOPERS.services.DeveloperService;

@SpringBootTest
@AutoConfigureMockMvc
class DeveloperControllerIntegrationTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	DeveloperRepository repo;
	
	Developer developer1;
	Developer developer2;
	Set<UserRole> setRole1;
		
	@BeforeEach
	void init() {
		setRole1 = new HashSet<UserRole>();
		setRole1.add(UserRole.USER);
		developer1 = new Developer("developer1", "password", "email1@gmail.com", null, null, setRole1 , null, null, null);
		repo.save(developer1);
		developer2 = new Developer("developer2", "password", "email2@gmail.com", null, null, setRole1, null, null, null);
	}
	
	
	@Test
	@DisplayName("Show developer list")
	void showDeveloperListTest() throws Exception {

		mockMvc.perform(get("/developers")).andExpect(status().isOk()).andExpect(jsonPath("$[0].username").value("master2"));
		repo.delete(developer1);
	}
	
	@Test
	@WithMockUser(username = "developer1", authorities = { "USER" })
	@DisplayName("Get developer by Username")
	void showDeveloperbyUsername() throws Exception {

		mockMvc.perform(get("/developers/" + "developer1")).andExpect(status().isOk()).andExpect(jsonPath("$.email").value(developer1.getEmail()));
		repo.delete(developer1);

	}
	
	@Test
	@DisplayName("Create developer")
	void createDeveloper() throws Exception {
		Gson gson = new Gson();
        String json = gson.toJson(developer2);
		mockMvc.perform(post("/developers/sign-up").contentType(MediaType.APPLICATION_JSON)
			    .content(json)).andExpect(status().isCreated());
		repo.delete(developer1);

	}
	
	@Test
	@DisplayName("Login as developer")
	void loginDeveloper() throws Exception {
		Developer developer3 = new Developer("developer3", "password", "email3@gmail.com", null, null, setRole1 , null, null, null);
		repo.save(developer3);
		mockMvc.perform(post("/developers/login?username=dummyDeveloper&password=dummyDeveloper")).andExpect(status().isOk());
		repo.delete(developer1);

	}

	@Test
	@WithMockUser(username = "developer1", authorities = { "USER" })
	@DisplayName("Edit developer test")
	void editDeveloper() throws Exception {
		developer1.setUsername("developerNumber1");
		repo.save(developer1);
		Gson gson = new Gson();
        String json = gson.toJson(developer1);
		mockMvc.perform(put("/developers"+"/edit/"+ developer1.getId()).contentType(MediaType.APPLICATION_JSON)
			    .content(json)).andExpect(status().isOk());
		repo.delete(developer1);

	}

	@Test
	@WithMockUser(username = "master2", authorities = { "USER","ADMIN" })
	@DisplayName("Delete developer test")
	void deleteDeveloper() throws Exception {
		String id = repo.findByUsername("dummyDeveloper2").get().getId();
		mockMvc.perform(delete("/developers/delete/"+ id)).andExpect(status().isNoContent());
	}

}
