package ISPP.G5.INDVELOPERS.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ISPP.G5.INDVELOPERS.dtos.GetDeveloperDTO;
import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.Publication;
import ISPP.G5.INDVELOPERS.models.Review;
import ISPP.G5.INDVELOPERS.services.SpamWordService;

@CrossOrigin("*")
@RestController
@RequestMapping("/spam")
public class SpamWordController {

	@Autowired
	private SpamWordService service;
	
	@GetMapping("/game")
	public ResponseEntity<Boolean> AnalyzeGame(@RequestBody Game game){
		try {
			return new ResponseEntity<>(service.CheckGame(game),HttpStatus.OK);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping("/review")
	public ResponseEntity<Boolean> AnalyzeReview(@RequestBody Review review){
		try {
			return new ResponseEntity<>(service.CheckReview(review),HttpStatus.OK);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping("/publication")
	public ResponseEntity<Boolean> AnalyzePublication(@RequestBody Publication publication){
		try {
			return new ResponseEntity<>(service.CheckPublication(publication),HttpStatus.OK);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping("/developer")
	public ResponseEntity<Boolean> AnalyzeDeveloper(@RequestBody Developer developer){
		try {
			return new ResponseEntity<>(service.CheckDeveloper(developer),HttpStatus.OK);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	@GetMapping("/developer")
	public ResponseEntity<Boolean> AnalyzeGetDeveloperDTO(@RequestBody GetDeveloperDTO developerDTO){
		try {
			return new ResponseEntity<>(service.CheckGetDeveloperDTO(developerDTO),HttpStatus.OK);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
}
