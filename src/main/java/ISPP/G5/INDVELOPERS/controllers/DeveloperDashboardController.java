package ISPP.G5.INDVELOPERS.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ISPP.G5.INDVELOPERS.models.DeveloperDashboard;
import ISPP.G5.INDVELOPERS.services.DeveloperDashboardService;



@CrossOrigin("*")
@RestController
@RequestMapping("/developerDashboard")
public class DeveloperDashboardController {
	
	@Autowired
	private DeveloperDashboardService		developerDashboardService;

	@GetMapping("/show")
	public ResponseEntity<DeveloperDashboard> showOne() throws NotFoundException {
		try {	
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(this.developerDashboardService.show());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

}
