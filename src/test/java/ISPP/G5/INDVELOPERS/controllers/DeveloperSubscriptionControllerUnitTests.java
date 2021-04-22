
package ISPP.G5.INDVELOPERS.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.DeveloperSubscription;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.DeveloperSubscriptionService;

@SpringBootTest
@AutoConfigureMockMvc
class DeveloperSubscriptionControllerUnitTests {

	@Autowired
	private MockMvc					mvc;

	@MockBean
	DeveloperSubscriptionService	service;

	@MockBean
	DeveloperService				devService;

	Developer						developer				= new Developer("hola", "adios", "email", null, new HashSet<UserRole>(), "description", "none", true, new ArrayList<Developer>());
	DeveloperSubscription			developerSubscription	= new DeveloperSubscription(developer, LocalDate.now(), LocalDate.of(2022, 5, 10));


	@Test
	@DisplayName("Is Premium Test")
	@WithMockUser(value = "spring")
	void testIsPremium() throws Exception {
		when(service.checkDeveloperHasSubscription(developer)).thenReturn(true);
		when(devService.findCurrentDeveloper()).thenReturn(developer);

		mvc.perform(get("/subscription/isPremium")).andExpect(status().isOk());

	}

	@Test
	@DisplayName("Buy Test")
	@WithMockUser(value = "spring")
	void testBuy() throws Exception {
		when(service.buySubscription(developer)).thenReturn("Correcto");
		when(devService.findCurrentDeveloper()).thenReturn(developer);

		mvc.perform(post("/subscription/buy")).andExpect(status().isCreated());
	}

	@Test
	@DisplayName("Check is Premium By id Test")
	@WithMockUser(value = "spring")
	void testCheckDeveloperIsPremiumById() throws Exception {
		when(service.checkDeveloperHasSubscription(developer)).thenReturn(true);
		when(devService.findById("ID")).thenReturn(developer);

		mvc.perform(get("/subscription/checkSubscription/ID")).andExpect(status().isOk());
	}

	@Test
	@DisplayName("Get Subscription Test")
	@WithMockUser(value = "spring")
	void testGetSubscriptionByDeveloperId() throws Exception {
		when(service.findByDeveloper(developer)).thenReturn(developerSubscription);
		when(devService.findById("ID")).thenReturn(developer);

		mvc.perform(get("/subscription/get/ID")).andExpect(status().isOk());
	}

}
