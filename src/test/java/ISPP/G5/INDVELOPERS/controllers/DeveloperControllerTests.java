
package ISPP.G5.INDVELOPERS.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
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


import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.services.DeveloperService;

@SpringBootTest
@AutoConfigureMockMvc
class DeveloperControllerTests {

	@Autowired
	private MockMvc				mockMvc;

	@MockBean
	private DeveloperService	developerService;

	Developer					developer1;
	Developer					developer2;


	@BeforeEach
	void init() {
		Set<UserRole> setRole1 = new HashSet<UserRole>();
		setRole1.add(UserRole.USER);
		developer1 = new Developer("developer1", "password", "email1@gmail.com", null, setRole1, null, null, null, new ArrayList<Developer>());
		developer1.setId("id1");
		developer2 = new Developer("developer2", "password", "email2@gmail.com", null, setRole1, null, null, null, new ArrayList<Developer>());
	}

	@Test
	@DisplayName("Show developer list")
	@WithMockUser(value = "spring")
	void showDeveloperListTest() throws Exception {

		when(developerService.getAll()).thenReturn(Lists.list(developer1, developer2));
		mockMvc.perform(get("/developers")).andExpect(status().isOk()).andExpect(jsonPath("$[0].username").value(developer1.getUsername())).andExpect(jsonPath("$[1].username").value(developer2.getUsername()));
	}

	@Test
	@DisplayName("Get developer by Username")
	@WithMockUser(value = "spring")
	void showDeveloperbyUsername() throws Exception {

		when(developerService.findByUsername("developer1")).thenReturn(developer1);
		mockMvc.perform(get("/developers/" + "developer1")).andExpect(status().isOk()).andExpect(jsonPath("$.email").value(developer1.getEmail()));
	}

	@Test
	@DisplayName("Create developer")
	@WithMockUser(value = "spring")
	void createDeveloper() throws Exception {
		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(developer1);
		mockMvc.perform(post("/developers/sign-up").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated());
	}

	@Test
	@DisplayName("Login as developer")
	@WithMockUser(value = "spring")
	void loginDeveloper() throws Exception {
		when(developerService.findByUsername("developer1")).thenReturn(developer1);
		mockMvc.perform(post("/developers/login?username=developer1&password=password")).andExpect(status().isOk());
	}

	/*Error test al cambiar a GetDTO*/
	@Test
	@DisplayName("Edit developer test")
	@WithMockUser(value = "spring")
	void editDeveloper() throws Exception {
		when(this.developerService.findById("id1")).thenReturn(developer1);
		when(this.developerService.updateDeveloper(any())).thenReturn(developer1);
		developer1.setUsername("developerNumber1");
		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(developer1);
		mockMvc.perform(put("/developers"+"/edit/"+ developer1.getId()).contentType(MediaType.APPLICATION_JSON)
			    .content(json)).andExpect(status().isOk());
	}

	@Test
	@DisplayName("Delete developer test")
	@WithMockUser(value = "spring")
	void deleteDeveloper() throws Exception {
		mockMvc.perform(delete("/developers/delete/" + developer1.getId())).andExpect(status().isOk());
	}

}
