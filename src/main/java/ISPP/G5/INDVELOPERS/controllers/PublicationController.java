
package ISPP.G5.INDVELOPERS.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Publication;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.PublicationService;

@RestController
@CrossOrigin("*")
@RequestMapping("/publications")
public class PublicationController {

	@Autowired
	private PublicationService	publicationService;

	@Autowired
	private DeveloperService	developService;


	@GetMapping("/findAll")
	public ResponseEntity<List<Publication>> getPublications() {
		try {
			return ResponseEntity.ok(publicationService.findAll());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/findByName")
	public ResponseEntity<List<Publication>> getPublicationsByUsername() {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			String username = userDetails.getUsername();

			return ResponseEntity.ok(publicationService.findByUSername(username));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/findById/{id}")
	public ResponseEntity<Publication> getPublicationById(@PathVariable final String id) {
		try {
			return ResponseEntity.ok(publicationService.findById(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@PostMapping("/add")
	public ResponseEntity<String> addPublication(@RequestBody final Publication publication) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			String user = userDetails.getUsername();
			Developer developer = developService.findByUsername(user);
			return new ResponseEntity<>(publicationService.addPublication(publication, developer), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deletePublicationById(@PathVariable final String id) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			String user = userDetails.getUsername();
			Developer developer = developService.findByUsername(user);
			Publication p = publicationService.findById(id);
			publicationService.deletePublication(p, developer);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}

}
