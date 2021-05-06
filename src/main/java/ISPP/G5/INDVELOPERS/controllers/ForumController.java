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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ISPP.G5.INDVELOPERS.dtos.ForumDTO;
import ISPP.G5.INDVELOPERS.mappers.ForumDTOConverter;
import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Forum;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.ForumService;

@RestController
@CrossOrigin("*")
@RequestMapping("/forums")
public class ForumController {

	@Autowired
	private ForumService service;
	
	@Autowired
	private DeveloperService developerService;

	@GetMapping("/findAll")
	public ResponseEntity<List<ForumDTO>> findAll() {
		try {
			return ResponseEntity.ok(ForumDTOConverter
					.convertListForumToListForumDTO(service.findAll()));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	
	@GetMapping("/findByTitle/{title}")
	public ResponseEntity<List<ForumDTO>> findByTitle(@PathVariable final String title) {
		try {
			return ResponseEntity.ok(ForumDTOConverter
					.convertListForumToListForumDTO(service.findByTitle(title)));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	
	@PostMapping("/add")
	public ResponseEntity<String> addForum(@RequestBody final Forum forum) {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) auth.getPrincipal();
			String user = userDetails.getUsername();
			Developer developer = developerService.findByUsername(user);
			return new ResponseEntity<>(service.addForum(forum, developer), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<HttpStatus> deleteForumById(@PathVariable("id") final String id) throws NotFoundException {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String user = userDetails.getUsername();
			Developer developer = developerService.findByUsername(user);
			Forum forum = service.findById(id);
			String response = service.deleteForum(forum, developer);
			if(response.equals("You don't have permissions to perform this action") ) {
					return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
}