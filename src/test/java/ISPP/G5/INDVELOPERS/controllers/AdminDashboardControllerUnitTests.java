
package ISPP.G5.INDVELOPERS.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import ISPP.G5.INDVELOPERS.models.AdminDashboard;
import ISPP.G5.INDVELOPERS.services.AdminDashboardService;

@SpringBootTest
@AutoConfigureMockMvc
class AdminDashboardControllerUnitTests {

	@Autowired
	private MockMvc			mvc;

	@MockBean
	AdminDashboardService	service;


	@Test
	@DisplayName("Show Test")
	@WithMockUser(value = "spring")
	void testShow() throws Exception {
		when(service.show()).thenReturn(new AdminDashboard(10, 30, 10, 34, 310., 100,5, 5, 10, 5, 5, 50, 50));

		mvc.perform(get("/adminDashboard/show")).andExpect(status().isOk());

	}

}
