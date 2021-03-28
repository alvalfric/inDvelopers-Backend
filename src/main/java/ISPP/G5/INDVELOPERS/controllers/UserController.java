package ISPP.G5.INDVELOPERS.controllers;

import ISPP.G5.INDVELOPERS.dtos.GetDeveloperDTO;
import ISPP.G5.INDVELOPERS.mappers.DeveloperDTOConverter;
import ISPP.G5.INDVELOPERS.models.Developer;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import ISPP.G5.INDVELOPERS.models.UserEntity;
import ISPP.G5.INDVELOPERS.services.UserEntityService;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("users")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

	private UserEntityService userService;

	@PostMapping("/sign-up")
	public ResponseEntity<UserEntity> newUser(@RequestBody UserEntity user) {
		try {

			return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.createUser(user));

		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestParam String username,
										@RequestParam String password) {
		try {
			return ResponseEntity.status(HttpStatus.OK)
					.body(this.userService.login(username,password));

		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@GetMapping
	public ResponseEntity<List<UserEntity>> getAll() {
		try {
			return ResponseEntity.ok(this.userService.getAll());
		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/{username}")
	public ResponseEntity<UserEntity> getProfileByUserName(@PathVariable String username) {
		try {
			return ResponseEntity.ok(this.userService.findByUsername(username));
		} catch(IllegalArgumentException | NotFoundException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}


}
