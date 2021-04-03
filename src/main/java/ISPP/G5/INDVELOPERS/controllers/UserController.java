package ISPP.G5.INDVELOPERS.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ISPP.G5.INDVELOPERS.models.UserEntity;
import ISPP.G5.INDVELOPERS.services.UserEntityService;
import lombok.AllArgsConstructor;

@CrossOrigin("*")
@RestController
@RequestMapping("users")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

	private UserEntityService userService;

	@PostMapping("/sign-up")
	public ResponseEntity<UserEntity> newUser(@RequestBody UserEntity user) {
		try {

			return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));

		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}


	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestParam String username,
		@RequestParam String password) {
		try {
			return ResponseEntity.status(HttpStatus.OK)
				.body(userService.login(username,password));

		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@GetMapping
	public ResponseEntity<List<UserEntity>> getAll() {
		try {
			return ResponseEntity.ok(userService.getAll());
		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/{username}")
	public ResponseEntity<UserEntity> getProfileByUserName(@PathVariable String username) {
		try {
			return ResponseEntity.ok(userService.findByUsername(username));
		} catch(IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}


}
