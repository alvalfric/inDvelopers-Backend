package ISPP.G5.INDVELOPERS.controllers;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.services.DeveloperService;

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
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
		return ResponseEntity.status(HttpStatus.OK).body(developerService.login(username, password));
	}

	@GetMapping
	public ResponseEntity<List<Developer>> getAll() {
		try {
			return ResponseEntity.ok(developerService.getAll());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

//	@GetMapping("/{username}")
//	public ResponseEntity<Developer> getProfileByUserName(@PathVariable String username) {
//		try {
//			return ResponseEntity.ok(developerService.findByUsername(username));
//		} catch (IllegalArgumentException e) {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//		}
//	}

	@GetMapping("/{username}")
	public ResponseEntity<GetDeveloperDTO> getProfileByUserName(@PathVariable String username) {
		try {
			return ResponseEntity.ok(DeveloperDTOConverter
					.DevelopertoGetDeveloperDTO(developerService.findByUsername(username)));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	@DeleteMapping("/delete/{developerId}")
	public ResponseEntity<String> deleteDeveloperById(@PathVariable("developerId") String developerId)
			throws NotFoundException {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(developerService.deleteDeveloper(developerId));
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/edit/{id}")
	public ResponseEntity<GetDeveloperDTO> updateDeveloper(@PathVariable String id,
			@RequestBody GetDeveloperDTO developerDTO) throws NotFoundException {
		Developer developer2 = this.developerService.findById(id);
		try {
			developer2.setUsername(developerDTO.getUsername());
			developer2.setUserImage(developerDTO.getUserImage());
			developer2.setTechnologies(developerDTO.getTechnologies());
			developer2.setDescription(developerDTO.getDescription());
			developer2.setEmail(developerDTO.getEmail());
			developer2.setDateOfBirth(developerDTO.getDateOfBirth());
			return ResponseEntity.ok(DeveloperDTOConverter
					.DevelopertoGetDeveloperDTO(this.developerService.updateDeveloper(developer2)));
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<GetDeveloperDTO>(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/changeToAdmin/{id}")
	public ResponseEntity<GetDeveloperDTO> changeToAdmin(@PathVariable String id) throws IllegalArgumentException {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Developer admin = this.developerService.findByUsername(userDetails.getUsername());
			if (!admin.getRoles().contains(UserRole.ADMIN)) {
				throw new IllegalArgumentException("Only an admin can upgrade an user to admin");
			}
			return ResponseEntity
					.ok(DeveloperDTOConverter.DevelopertoGetDeveloperDTO(this.developerService.changeToAdmin(id)));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return new ResponseEntity<GetDeveloperDTO>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/changeToUser/{id}")
	public ResponseEntity<GetDeveloperDTO> changeToUser(@PathVariable String id) throws NotFoundException {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Developer admin = this.developerService.findByUsername(userDetails.getUsername());
			if (!admin.getRoles().contains(UserRole.ADMIN)) {
				throw new IllegalArgumentException("Only an admin can downgrade an user to user");
			}
			return ResponseEntity
					.ok(DeveloperDTOConverter.DevelopertoGetDeveloperDTO(this.developerService.changeToUser(id)));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return new ResponseEntity<GetDeveloperDTO>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/me")
	public ResponseEntity<GetDeveloperDTO> whoIAm(
			@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
		try {
			return ResponseEntity.ok(DeveloperDTOConverter
					.DevelopertoGetDeveloperDTO(developerService.findByUsername(principal.getUsername())));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	/* Following users implementation*/
	
	@PutMapping("/follow/{username}")
	public ResponseEntity<String> followUsername(
			@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, @PathVariable String username) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(developerService.followDeveloper(username));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@PutMapping("/unfollow/{username}")
	public ResponseEntity<String> unfollowUsername(
			@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal, @PathVariable String username) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(developerService.unfollowDeveloper(username));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	@GetMapping("/me/myFollowers")
	public ResponseEntity<List<GetDeveloperDTO>> myFollowers(
			@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(this.developerService.getMyFollowersDTO(principal.getUsername()));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	@GetMapping("/me/myFollowed")
	public ResponseEntity<List<GetDeveloperDTO>> myFollowed(
			@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(this.developerService.getMyFollowedDTO(principal.getUsername()));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
}
