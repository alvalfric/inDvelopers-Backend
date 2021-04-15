package ISPP.G5.INDVELOPERS.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ISPP.G5.INDVELOPERS.models.AdminDashboard;
import ISPP.G5.INDVELOPERS.services.AdminDashboardService;

@CrossOrigin("*")
@RestController
@RequestMapping("/adminDashboard")
public class AdminDashboardController {

	@Autowired
	private AdminDashboardService adminDashboardService;

	@Autowired
	public AdminDashboardController(final AdminDashboardService adminDashboardService) {
		this.adminDashboardService = adminDashboardService;

	}

	@GetMapping("/show")
	public ResponseEntity<AdminDashboard> show() throws NotFoundException {
		try {
			return ResponseEntity.ok(this.adminDashboardService.show());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

}
