
package ISPP.G5.INDVELOPERS.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

import ISPP.G5.INDVELOPERS.models.UserEntity;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.services.UserEntityService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerUnitTests {

	@Autowired
	private MockMvc				mockMvc;

	@MockBean
	private UserEntityService	userService;

	UserEntity					user	= new UserEntity("master2", "password", "email", new HashSet<UserRole>(), true);


	@Test
	@DisplayName("Sign-up user")
	@WithMockUser(value = "spring")
	void signUpUserTest() throws Exception {
		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(user);

		when(userService.createUser(any(UserEntity.class))).thenReturn(user);
		mockMvc.perform(post("/users/sign-up").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated());
	}

	@Test
	@DisplayName("Login user")
	@WithMockUser(value = "spring")
	void loginUserTest() throws Exception {
		when(userService.login(any(String.class), any(String.class))).thenReturn("OK");
		mockMvc.perform(post("/users/login").param("username", "username").param("password", "password")).andExpect(status().isOk());
	}

	@Test
	@DisplayName("Get All user")
	@WithMockUser(value = "spring")
	void getAllTest() throws Exception {
		when(userService.getAll()).thenReturn(new ArrayList<UserEntity>());
		mockMvc.perform(get("/users")).andExpect(status().isOk());
	}

	@Test
	@DisplayName("Get Profile by Username user")
	@WithMockUser(value = "spring")
	void getProfileByUsernameTest() throws Exception {
		when(userService.findByUsername(any(String.class))).thenReturn(user);
		mockMvc.perform(get("/users/username")).andExpect(status().isOk());
	}

	@Test
	@DisplayName("Bad Requests")
	@WithMockUser(value = "spring")
	void badRequestTest() throws Exception {
		when(userService.findByUsername(any(String.class))).thenThrow(IllegalArgumentException.class);
		when(userService.createUser(any(UserEntity.class))).thenThrow(IllegalArgumentException.class);
		when(userService.getAll()).thenThrow(IllegalArgumentException.class);

		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(user);

		mockMvc.perform(get("/users")).andExpect(status().isBadRequest());
		mockMvc.perform(get("/users/username")).andExpect(status().isBadRequest());
		mockMvc.perform(post("/users/sign-up").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isBadRequest());

	}

}
