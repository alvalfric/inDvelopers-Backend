package ISPP.G5.INDVELOPERS.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ISPP.G5.INDVELOPERS.models.AdminDashboard;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.Publication;
import ISPP.G5.INDVELOPERS.services.AdminDashboardService;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.GameService;

@CrossOrigin("*")
@RestController
@RequestMapping("/adminDashboard")
public class AdminDashboardController {
	
	@Autowired
	private AdminDashboardService			adminDashboardService;
	
	@Autowired
	public AdminDashboardController(final AdminDashboardService adminDashboardService) {
		this.adminDashboardService = adminDashboardService;
		
	}
	
	@GetMapping("/showOne")
	public ResponseEntity<AdminDashboard> showOne() throws NotFoundException {
		try {
			return ResponseEntity.ok(adminDashboardService.showOne());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

}
