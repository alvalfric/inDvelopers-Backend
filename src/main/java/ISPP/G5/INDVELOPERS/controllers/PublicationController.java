package ISPP.G5.INDVELOPERS.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Publication;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.PublicationService;

@RestController
@RequestMapping("/publications")
public class PublicationController {

	@Autowired
	private PublicationService publicationService;
	
	@Autowired
	private DeveloperService developService;

	@GetMapping("/findAll")
	public List<Publication> getPublications() {
		return this.publicationService.findAll();

	}

	@GetMapping("/findByName/{username}")
	public ResponseEntity<List<Publication>> getPublicationsByUsername(@PathVariable String username) {
		
		try {
			return ResponseEntity.ok(this.publicationService.findByUSername());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/findById/{id}")
	public ResponseEntity<Publication> getPublicationById(@PathVariable String id) {
		try {
			return ResponseEntity.ok(this.publicationService.findById(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@PostMapping("/add")
	public String addPrueba(@RequestBody Publication publication) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		String user = userDetails.getUsername();
		try {
			Developer developer = this.developService.findByUsername(user);
			return this.publicationService.addPublication(publication, developer);
		}catch(NotFoundException e) {
			throw new IllegalArgumentException("Publication couldn't be created correctly.");
		}
		
		
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deletePublicationById(@PathVariable String id) {
		try {
			Publication p = this.publicationService.findById(id);
			this.publicationService.deletePublication(p);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			throw new IllegalArgumentException("Publication couldn't be deleted.");
		}

	}

}
