package ISPP.G5.INDVELOPERS.controllers;

import ISPP.G5.INDVELOPERS.dtos.CommentaryDTO;
import ISPP.G5.INDVELOPERS.dtos.ForumDTO;
import ISPP.G5.INDVELOPERS.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ISPP.G5.INDVELOPERS.dtos.GetDeveloperDTO;
import ISPP.G5.INDVELOPERS.services.SpamWordService;

@CrossOrigin("*")
@RestController
@RequestMapping("/spam")
public class SpamWordController {

	@Autowired
	private SpamWordService service;
	
	@PostMapping("/game")
	public ResponseEntity<Boolean> AnalyzeGame(@RequestBody Game game){
		try {
			return new ResponseEntity<>(service.CheckGame(game),HttpStatus.OK);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	@PostMapping("/review")
	public ResponseEntity<Boolean> AnalyzeReview(@RequestBody Review review){
		try {
			return new ResponseEntity<>(service.CheckReview(review),HttpStatus.OK);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	@PostMapping("/publication")
	public ResponseEntity<Boolean> AnalyzePublication(@RequestBody Publication publication){
		try {
			return new ResponseEntity<>(service.CheckPublication(publication),HttpStatus.OK);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	@PostMapping("/signupDeveloper")
	public ResponseEntity<Boolean> AnalyzeDeveloper(@RequestBody Developer developer){
		try {
			return new ResponseEntity<>(service.CheckDeveloper(developer),HttpStatus.OK);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	@PostMapping("/editDeveloper")
	public ResponseEntity<Boolean> AnalyzeGetDeveloperDTO(@RequestBody GetDeveloperDTO developerDTO){
		try {
			return new ResponseEntity<>(service.CheckGetDeveloperDTO(developerDTO),HttpStatus.OK);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/forum")
	public ResponseEntity<Boolean> AnalyzeForum(@RequestBody ForumDTO forumDTO){
		try {
			return new ResponseEntity<>(service.CheckForum(forumDTO),HttpStatus.OK);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/post")
	public ResponseEntity<Boolean> AnalyzeCommentary(@RequestBody CommentaryDTO commentaryDTO){
		try {
			return new ResponseEntity<>(service.CheckCommentary(commentaryDTO),HttpStatus.OK);
		}catch(IllegalArgumentException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
}
