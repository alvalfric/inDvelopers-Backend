package ISPP.G5.INDVELOPERS.paypal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payee;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.orders.AmountWithBreakdown;
import com.paypal.orders.ApplicationContext;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.PurchaseUnitRequest;

@Service
public class PaypalService {
	@Value("${paypal.client.id}")
	private String clientId;
	@Value("${paypal.client.secret}")
	private String clientSecret;
	@Value("${paypal.mode}")
	private String mode;
	@Autowired
	private APIContext apiContext;

	
	public Map<String, String> paypalSdkConfig() {
		Map<String, String> configMap = new HashMap<>();
		configMap.put("mode", mode);
		return configMap;
	}

	
	public OAuthTokenCredential oAuthTokenCredential() {
		return new OAuthTokenCredential(clientId, clientSecret, paypalSdkConfig());
	}

	
	public APIContext apiContext() throws PayPalRESTException {
		String requestID = UUID.randomUUID().toString();
		APIContext context = new APIContext(oAuthTokenCredential().getAccessToken(), requestID);
		context.setConfigurationMap(paypalSdkConfig());
		return context;
	}

	public Payment createPayment(Double total, String currency, String method, String intent, String description,
			String cancelUrl, String successUrl, String payeeEmail) throws PayPalRESTException {
		String requestID = UUID.randomUUID().toString();
		APIContext context = new APIContext(oAuthTokenCredential().getAccessToken(), requestID);
		context.setConfigurationMap(paypalSdkConfig());
		Amount amount = new Amount();
		amount.setCurrency(currency);
		// total = new BigDecimal(total).setScale(2,
		// RoundingMode.HALF_UP).doubleValue();
		amount.setTotal(total.toString());

		Transaction transaction = new Transaction();
		transaction.setDescription(description);
		transaction.setAmount(amount);
		Payee payee = new Payee();
		payee.setEmail(payeeEmail);// Email del developer al que se le va a pagar
		transaction.setPayee(payee);

		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction);

		Payer payer = new Payer();
		payer.setPaymentMethod(method.toString());

		Payment payment = new Payment();
		payment.setIntent(intent.toString());
		payment.setPayer(payer);
		payment.setTransactions(transactions);
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl(cancelUrl);
		redirectUrls.setReturnUrl(successUrl);
		payment.setRedirectUrls(redirectUrls);

		this.apiContext.setMaskRequestId(false);
		this.apiContext.getRequestId();
		return payment.create(context);
	}

	public Payment createPaymentToUs(Double total, String currency, String method, String intent, String description,
			String cancelUrl, String successUrl) throws PayPalRESTException {
		String requestID = UUID.randomUUID().toString();
		APIContext context = new APIContext(oAuthTokenCredential().getAccessToken(), requestID);
		context.setConfigurationMap(paypalSdkConfig());
		Amount amount = new Amount();
		amount.setCurrency(currency);
		// total = new BigDecimal(total).setScale(2,
		// RoundingMode.HALF_UP).doubleValue();
		amount.setTotal(total.toString());

		Transaction transaction = new Transaction();
		transaction.setDescription(description);
		transaction.setAmount(amount);

		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction);

		Payer payer = new Payer();
		payer.setPaymentMethod(method.toString());

		Payment payment = new Payment();
		payment.setIntent(intent.toString());
		payment.setPayer(payer);
		payment.setTransactions(transactions);
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl(cancelUrl);
		redirectUrls.setReturnUrl(successUrl);
		payment.setRedirectUrls(redirectUrls);

		this.apiContext.setMaskRequestId(false);
		this.apiContext.getRequestId();
		return payment.create(context);
	}

	public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
		String requestID = UUID.randomUUID().toString();
		APIContext context = new APIContext(oAuthTokenCredential().getAccessToken(), requestID);
		context.setConfigurationMap(paypalSdkConfig());
		Payment payment = new Payment();
		payment.setId(paymentId);
		PaymentExecution paymentExecute = new PaymentExecution();
		paymentExecute.setPayerId(payerId);
		this.apiContext.setMaskRequestId(false);
		this.apiContext.getRequestId();
		return payment.execute(context, paymentExecute);
	}

}
