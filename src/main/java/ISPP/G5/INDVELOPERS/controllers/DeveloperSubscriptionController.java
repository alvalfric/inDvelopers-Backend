package ISPP.G5.INDVELOPERS.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.DeveloperSubscription;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.DeveloperSubscriptionService;

@CrossOrigin("*")
@RestController
@RequestMapping("/subscription")
public class DeveloperSubscriptionController {

	private DeveloperSubscriptionService developerSubscriptionService;
	private DeveloperService developerService;

	@Autowired
	public DeveloperSubscriptionController(final DeveloperService developerService, final DeveloperSubscriptionService developerSubscriptionService) {
		this.developerService = developerService;
		this.developerSubscriptionService = developerSubscriptionService;
	}

	@GetMapping("/isPremium")
	public ResponseEntity<Boolean> checkHasSubscription() {
		try {
			Developer developer = this.developerService.findCurrentDeveloper();

			return ResponseEntity.ok(this.developerSubscriptionService.checkDeveloperHasSubscription(developer));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@PostMapping("/buy")
	public ResponseEntity<String> buySubscription() {
		try {
			Developer developer = developerService.findCurrentDeveloper();

			return ResponseEntity.status(HttpStatus.CREATED)
				.body(this.developerSubscriptionService.buySubscription(developer));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/checkSubscription/{developerId}")
	public ResponseEntity<Boolean> checkDeveloperIsPremiumById(@PathVariable String developerId) {
		try {
			Developer developer = developerService.findById(developerId);
			
			if(developer == null) {
				return ResponseEntity.status(HttpStatus.OK).body(false);
			}
			
			return ResponseEntity.status(HttpStatus.OK).body((this.developerSubscriptionService.checkDeveloperHasSubscription(developer)));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	@GetMapping("/get/{developerId}")
	public ResponseEntity<DeveloperSubscription> getDeveloperSubscription(@PathVariable String developerId) {
		try {
			Developer developer = developerService.findById(developerId);
			
			if(developer == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}
			
			return ResponseEntity.status(HttpStatus.OK).body(this.developerSubscriptionService.findByDeveloper(developer));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

}