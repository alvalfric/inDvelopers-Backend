package ISPP.G5.INDVELOPERS.controllers;

import ISPP.G5.INDVELOPERS.dtos.GetDeveloperDTO;
import ISPP.G5.INDVELOPERS.mappers.DeveloperDTOConverter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.services.DeveloperService;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/developers")
public class DeveloperController {

	@Autowired
	private DeveloperService developerService;
	
	
	@Autowired
	public DeveloperController(DeveloperService developerService) {
		super();
		this.developerService = developerService;
	}


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
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<HttpStatus> deleteDeveloperById(@PathVariable("id") String id) throws NotFoundException{
		try {
			this.developerService.deleteDeveloper(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch(IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/edit/{id}")
	public ResponseEntity<Developer> updateDeveloper(@PathVariable String id, @RequestBody Developer developer) throws NotFoundException{
		Developer developer2 = this.developerService.findById(id);
		try {
			developer2.setUsername(developer.getUsername());
			developer2.setUserImage(developer.getUserImage());
			developer2.setTechnologies(developer.getTechnologies());
			developer2.setDescription(developer.getDescription());
			 return new ResponseEntity<>(this.developerService.updateDeveloper(developer2), HttpStatus.OK);
		} catch(IllegalArgumentException e){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/me")
	public ResponseEntity<GetDeveloperDTO> whoIAm(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
		try {
			return ResponseEntity.ok((DeveloperDTOConverter.DevelopertoGetDeveloperDTO(
					developerService.findByUsername(principal.getUsername()))));
		} catch(IllegalArgumentException | NotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

}
