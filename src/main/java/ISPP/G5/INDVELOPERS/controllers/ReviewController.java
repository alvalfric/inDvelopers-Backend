
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

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.Review;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.GameService;
import ISPP.G5.INDVELOPERS.services.ReviewService;

@CrossOrigin("*")
@RestController
@RequestMapping("/reviews")
public class ReviewController {

	@Autowired
	private ReviewService		service;

	@Autowired
	private DeveloperService	developerService;

	@Autowired
	private GameService			gameService;


	@GetMapping("/game/{gameId}")
	public ResponseEntity<List<Review>> findAllByGame(@PathVariable("gameId") final String gameId) {
		try {
			return ResponseEntity.ok(service.findAllByGameId(gameId));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	@GetMapping("/{id}")
	public ResponseEntity<Review> findById(@PathVariable("id") final String id) throws NotFoundException {
		try {
			return ResponseEntity.ok(service.findById(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	@PostMapping("/game/{gameId}/add")
	public ResponseEntity<String> addReview(@RequestBody final Review review, @PathVariable("gameId") final String gameId) throws NotFoundException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Developer developer = developerService.findByUsername(userDetails.getUsername());
		Game game = gameService.findById(gameId);
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(service.addReview(review, game, developer));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	@PutMapping("/{id}/edit")
	public ResponseEntity<String> editReview(@RequestBody final Review review, @PathVariable("id") final String id) throws NotFoundException {
		Review r = service.findById(id);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Developer developer = developerService.findByUsername(userDetails.getUsername());
		if (!review.getDeveloper().getId().equals(developer.getId()))
			throw new IllegalArgumentException("Only the creator of the review can edit it");
		try {
			r.setDeveloper(review.getDeveloper());
			r.setGame(review.getGame());
			r.setScore(review.getScore());
			r.setText(review.getText());
			return new ResponseEntity<>(service.updateReview(r), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	@DeleteMapping("/{id}/delete")
	public ResponseEntity<HttpStatus> deleteReview(@PathVariable final String id) throws NotFoundException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Developer developer = developerService.findByUsername(userDetails.getUsername());
		if (!service.findById(id).getDeveloper().getId().equals(developer.getId()))
			throw new IllegalArgumentException("Only the creator of the review can remove it");
		try {
			service.deleteReview(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
