package ISPP.G5.INDVELOPERS.paypal;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payee;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.orders.AmountWithBreakdown;
import com.paypal.orders.ApplicationContext;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.PurchaseUnitRequest;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.Order;
import ISPP.G5.INDVELOPERS.models.Publication;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.GameService;
import ISPP.G5.INDVELOPERS.services.OwnedGameService;

@RestController
@CrossOrigin("*")
@RequestMapping("/payments")
public class PaypalController {

	@Autowired
	PaypalService service;

	@Autowired
	private OrderService orderService;

	@Autowired
	private GameService gameService;
	
	@Autowired
	private OwnedGameService ownedGameService;
	
	@Autowired
	private DeveloperService developerService;

	public static final String SUCCESS_URL = "/success";
	public static final String CANCEL_URL = "/cancel";

	@GetMapping(value = "/{gameId}")
	public ResponseEntity<Order> summary(@PathVariable String gameId) {
		try {
			Game game = gameService.findById(gameId);
			String email = game.getCreator().getEmail();
			Order order = new Order(game.getPrice(), "EUR", "Paypal", "Sale", "This is a pay for a game.",email);
			this.orderService.save(order);
			return ResponseEntity.ok(order);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

	}
	// comprobaciones con entidad order
//	@GetMapping("/findById/{id}")
//	public ResponseEntity<Order> getorderById(@PathVariable final String id) {
//		try {
//			return ResponseEntity.ok(orderService.findById(id));
//		} catch (IllegalArgumentException e) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//		}
//	}
//	
//	@PostMapping("/add")
//	public ResponseEntity<Order> addPublication(@RequestBody final Order order) {
//		try {
//			return new ResponseEntity<>(orderService.save(order), HttpStatus.OK);
//		} catch (IllegalArgumentException e) {
//			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//		}
//
//	}

	@PostMapping(value = "/pay")
	public ResponseEntity<String> payment(@RequestBody Order order) {
		String linkRef = null;
		try {
			Payment payment = service.createPayment(order.getPrice(), order.getCurrency(), order.getMethod(),
					order.getIntent(), order.getDescription(), "http://localhost:8080" + CANCEL_URL,
					"http://localhost:8080" + SUCCESS_URL, order.getPayeeEmail());
			Payee payee = new Payee();
			payee.setEmail(order.getPayeeEmail()); //Email del developer al que se le va a pagar
			payment.setPayee(payee);
			for (Links link : payment.getLinks()) {
				if (link.getRel().equals("approval_url")) {
					linkRef = link.getHref();
				}
			}
			return new ResponseEntity<>(linkRef, HttpStatus.OK);
		} catch (PayPalRESTException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping(value = CANCEL_URL)
	public ResponseEntity<String> cancelPay() {
		return new ResponseEntity<String>("The payment was canceled.", HttpStatus.BAD_REQUEST);
	}

	@GetMapping(value = SUCCESS_URL)
	public ResponseEntity<String> successPay(@RequestParam("paymentId") String paymentId,
			@RequestParam("PayerID") String payerId, @RequestParam("gameId") String gameId) {
		String res = null;
		try {
			Payment payment = service.executePayment(paymentId, payerId);
			if (payment.getState().equals("approved")) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				UserDetails userDetails = (UserDetails) auth.getPrincipal();
				String user = userDetails.getUsername();
				Developer developer = developerService.findByUsername(user);
				ownedGameService.buyGameByDeveloperAndGameId(developer, gameId);
				res = "The transaccion was successful.";
			}
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch (PayPalRESTException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}

	
}
