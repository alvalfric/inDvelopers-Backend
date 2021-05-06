package ISPP.G5.INDVELOPERS.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ISPP.G5.INDVELOPERS.models.Commentary;
import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.services.CommentaryService;
import ISPP.G5.INDVELOPERS.services.DeveloperService;

@RestController
@CrossOrigin("*")
@RequestMapping("/comments")
public class CommentaryController {

	
	@Autowired
	private CommentaryService service;
	
	@Autowired
	private DeveloperService developerService;
	
	@GetMapping("/findByForum/{idForum}")
	public ResponseEntity<List<Commentary>> findForum(@PathVariable("idForum") final String id) {
		try {
			return ResponseEntity.ok(service.findByForum(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	@GetMapping("/findById/{id}")
	public ResponseEntity<Commentary> findById(@PathVariable final String id) {
		try {
			return ResponseEntity.ok(service.findById(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	
	@PostMapping("/add/{idForum}")
	public ResponseEntity<String> addForum(@PathVariable("idForum") final String idForum,@RequestBody final Commentary commentary) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			String user = userDetails.getUsername();
			Developer developer = developerService.findByUsername(user);
			return new ResponseEntity<>(service.addCommentary(commentary,idForum, developer), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}
	
	@PutMapping("/edit/{id}")
	public ResponseEntity<String> updateGame(@PathVariable("id") final String id, @RequestBody final Commentary commentary){
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String user = userDetails.getUsername();
			Developer developer = developerService.findByUsername(user);
			return new ResponseEntity<>(service.updateCommentary(id,commentary, developer), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<HttpStatus> deletePublicationById(@PathVariable("id") final String id) throws NotFoundException {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String user = userDetails.getUsername();
			Developer developer = developerService.findByUsername(user);
			service.deleteCommentary(id, developer);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
}
