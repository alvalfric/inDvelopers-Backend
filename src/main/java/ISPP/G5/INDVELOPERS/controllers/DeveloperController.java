package ISPP.G5.INDVELOPERS.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.services.DeveloperService;

import java.util.List;

@RestController
@RequestMapping("/developers")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DeveloperController {

	private DeveloperService developerService;

	@PostMapping("/sign-up")
	public ResponseEntity<Developer> newDeveloper(@RequestBody Developer developer) {
		try {

			return ResponseEntity.status(HttpStatus.CREATED).body(this.developerService.createDeveloper(developer));

		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestParam String username,
										@RequestParam String password) {
		try {
			return ResponseEntity.status(HttpStatus.OK)
					.body(this.developerService.login(username,password));

		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@GetMapping
	public ResponseEntity<List<Developer>> getAll() {
		try {
			return ResponseEntity.ok(this.developerService.getAll());
		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/{username}")
	public ResponseEntity<Developer> getProfileByUserName(@PathVariable String username) {
		try {
			return ResponseEntity.ok(this.developerService.findByUsername(username));
		} catch(IllegalArgumentException | NotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}


}
