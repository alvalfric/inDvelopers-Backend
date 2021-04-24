package ISPP.G5.INDVELOPERS.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import ISPP.G5.INDVELOPERS.controllers.AdminDashboardController;
import ISPP.G5.INDVELOPERS.models.AdminDashboard;

@SpringBootTest
public class AdminDashboardControllerIntegrationTests {

	@Autowired
	private AdminDashboardController adminDashboardController;
	
	@Test
	@DisplayName("Find games by developer followed")
	@WithMockUser(username = "master2", authorities = { "ADMIN" })
	void testShowAdminDashboard() throws Exception{
		ResponseEntity<AdminDashboard> responseAdminDashboard = this.adminDashboardController.show();
		Assertions.assertEquals(responseAdminDashboard.getStatusCodeValue(), 200);
		Assertions.assertNotNull(responseAdminDashboard.getBody());
		Assertions.assertEquals(responseAdminDashboard.getBody().getTotalGamesCreated(), 3);
		Assertions.assertEquals(responseAdminDashboard.getBody().getTotalDevelopers(), 14);
		Assertions.assertEquals(responseAdminDashboard.getBody().getTotalPremiumUsers(), 11);
		Assertions.assertEquals(responseAdminDashboard.getBody().getTotalMoneyEarnedByDevelopers(), 47.08);

	}
}
