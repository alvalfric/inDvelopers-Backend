
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
			return new ResponseEntity<>(service.findAllByGameId(gameId), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping("/{id}")
	public ResponseEntity<Review> findById(@PathVariable("id") final String id) {
		try {
			return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	@PostMapping("/game/{gameId}/add")
	public ResponseEntity<String> addReview(@RequestBody final Review review, @PathVariable("gameId") final String gameId) throws NotFoundException {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Developer developer = developerService.findByUsername(userDetails.getUsername());
			Game game = gameService.findById(gameId);

			return new ResponseEntity<>(service.addReview(review, game, developer), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	@PutMapping("/edit/{id}")
	public ResponseEntity<String> editReview(@RequestBody final Review review, @PathVariable("id") final String id) throws NotFoundException {
		try {
      //Review oldReview = service.findById(id);
	  Review oldReview = null;
	  List<Review> gameReviews = service.findAllByGameId(id);
	  for (Review r : gameReviews) {
	  	if(r.getDeveloper().getUsername().equals(review.getDeveloper().getUsername()))
	  		oldReview = r;
	  }
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      Developer developer = developerService.findByUsername(userDetails.getUsername());
      if (!oldReview.getDeveloper().getId().equals(developer.getId()))
        throw new IllegalArgumentException("Only the creator of the review can edit a review!");
			oldReview.setScore(review.getScore());
			oldReview.setText(review.getText());
			oldReview.setEdited(review.getEdited());
			return new ResponseEntity<>(service.updateReview(oldReview), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteReview(@PathVariable("id") final String id) throws NotFoundException {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			Developer developer = developerService.findByUsername(userDetails.getUsername());
			if (!service.findById(id).getDeveloper().getId().equals(developer.getId()))
				throw new IllegalArgumentException("Only the creator of the review can remove it");

			return new ResponseEntity<>(service.deleteReview(id), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
}
