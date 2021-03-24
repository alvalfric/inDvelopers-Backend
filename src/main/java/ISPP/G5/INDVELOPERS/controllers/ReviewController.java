
package ISPP.G5.INDVELOPERS.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ISPP.G5.INDVELOPERS.models.Review;
import ISPP.G5.INDVELOPERS.services.ReviewService;

@CrossOrigin("*")
@RestController
@RequestMapping("/Review")
public class ReviewController {

	@Autowired
	private ReviewService service;


	@GetMapping("/communicate")
	public String communicateWithReact() {
		return this.service.communicateWithReact();
	}
	@GetMapping("/findAll")
	public List<Review> findAll() {
		return this.service.findAll();
	}
	@GetMapping("/{id}")
	public Review findById(@PathVariable final String id) {
		return this.service.findById(id);
	}
	@PostMapping("/add")
	public String addReview(@RequestBody final Review review) {
		return this.service.saveReview(review);
	}
	@PutMapping("/{id}/edit")
	public String editReview(@RequestBody final Review review, @PathVariable final String id) {
		return this.service.saveReview(review);
	}
	@DeleteMapping("/{id}/delete")
	public String deleteReview(@PathVariable final String id) {
		return this.service.deleteReview(id);
	}
}
