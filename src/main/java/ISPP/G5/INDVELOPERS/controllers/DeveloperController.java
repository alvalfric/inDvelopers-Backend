package ISPP.G5.INDVELOPERS.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ISPP.G5.INDVELOPERS.dtos.GetDeveloperDTO;
import ISPP.G5.INDVELOPERS.mappers.DeveloperDTOConverter;
import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import lombok.AllArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("/developers")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DeveloperController {

	private DeveloperService developerService;

	@PostMapping("/sign-up")
	public ResponseEntity<Developer> newDeveloper(@RequestBody Developer developer) {
		try {

			return ResponseEntity.status(HttpStatus.CREATED).body(developerService.createDeveloper(developer));

		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}


	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestParam String username,
		@RequestParam String password) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(developerService.login(username,password));
	}

	@GetMapping
	public ResponseEntity<List<Developer>> getAll() {
		try {
			return ResponseEntity.ok(developerService.getAll());
		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/{username}")
	public ResponseEntity<Developer> getProfileByUserName(@PathVariable String username) {
		try {
			return ResponseEntity.ok(developerService.findByUsername(username));
		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<HttpStatus> deleteDeveloperById(@PathVariable("id") String id) throws NotFoundException{
		try {
			return new ResponseEntity<>(HttpStatus.OK);
		} catch(IllegalArgumentException e) {
			return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/edit/{id}")
	public ResponseEntity<Developer> updateDeveloper(@PathVariable String id, @RequestBody Developer developer) throws NotFoundException{
		try {
			Developer developer2 = developerService.findById(id);
			developer2.setUsername(developer.getUsername());
			developer2.setUserImage(developer.getUserImage());
			developer2.setTechnologies(developer.getTechnologies());
			developer2.setDescription(developer.getDescription());
			return new ResponseEntity<>(developerService.updateDeveloper(developer2), HttpStatus.OK);
		} catch(IllegalArgumentException e){
			return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/me")
	public ResponseEntity<GetDeveloperDTO> whoIAm(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
		try {
			return ResponseEntity.ok(DeveloperDTOConverter.DevelopertoGetDeveloperDTO(
				developerService.findByUsername(principal.getUsername())));
		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

}
