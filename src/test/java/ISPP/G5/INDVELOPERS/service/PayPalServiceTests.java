
package ISPP.G5.INDVELOPERS.service;

import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import com.paypal.base.rest.OAuthTokenCredential;

import ISPP.G5.INDVELOPERS.paypal.PaypalService;

@SpringBootTest
class PayPalServiceTests {

	@InjectMocks
	PaypalService service;


	@Test
	@DisplayName("Confing SDK")
	void testSDKConfig() {
		Map<String, String> res = service.paypalSdkConfig();

		assertNotNull(res);
	}

	@Test
	@DisplayName("OAuthTokenCredential")
	void testOAuthTokenCredential() {
		OAuthTokenCredential res = service.oAuthTokenCredential();

		assertNotNull(res);
	}

}
