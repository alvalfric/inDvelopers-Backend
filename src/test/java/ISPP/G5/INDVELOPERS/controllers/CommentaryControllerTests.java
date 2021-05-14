package ISPP.G5.INDVELOPERS.controllers;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ISPP.G5.INDVELOPERS.controllers.CommentaryController;
import ISPP.G5.INDVELOPERS.dtos.CommentaryDTO;
import ISPP.G5.INDVELOPERS.models.Commentary;
import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Forum;
import ISPP.G5.INDVELOPERS.services.CommentaryService;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.ForumService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentaryControllerTests {

	@Autowired
	private CommentaryController commentaryController;

	@Autowired
	private CommentaryService commentaryService;

	@Autowired
	private ForumService forumService;

	@Autowired
	private DeveloperService developerService;

	@Test
	void testFindAll() throws Exception {
		Forum forum = this.forumService.findAll().get(0);
		ResponseEntity<List<CommentaryDTO>> responseFind = this.commentaryController.findForum(forum.getId());
		Assertions.assertEquals(responseFind.getStatusCodeValue(), 200);
		Assertions.assertNotNull(responseFind.getBody());
		Assertions.assertEquals(responseFind.getBody().get(0).getDescription(),
				"I'm looking for video games for nintendo");
	}

	@Test
	void testFindById() throws Exception {
		Commentary comment = this.commentaryService.findAll().get(0);
		ResponseEntity<CommentaryDTO> responseFind = this.commentaryController.findById(comment.getId());
		Assertions.assertEquals(responseFind.getStatusCodeValue(), 200);
		Assertions.assertNotNull(responseFind.getBody());
		Assertions.assertEquals(responseFind.getBody().getDescription(), "I'm looking for video games for nintendo");
	}

	@WithMockUser(username = "alvaro", authorities = { "USER" })
	@Test
	void testAddCommentary() throws Exception {
		Forum forum = this.forumService.findAll().get(0);
		Developer developer = this.developerService.findByUsername("alvaro");
		Commentary comment = new Commentary("I love games", new Date(), false, developer, forum);

		ResponseEntity<String> responseFind = this.commentaryController.addForum(forum.getId(), comment);
		Assertions.assertEquals(responseFind.getStatusCodeValue(), 200);
		Assertions.assertNotNull(responseFind.getBody());
	}

	@WithMockUser(username = "alvaro", authorities = { "USER" })
	@Test
	void testEditCommentary() throws Exception {
		Commentary comment = this.commentaryService.findAll().get(0);
		comment.setDescription("Changed description");

		ResponseEntity<String> responseFind = this.commentaryController.updateGame(comment.getId(), comment);
		Assertions.assertEquals(responseFind.getStatusCodeValue(), 200);
		Assertions.assertNotNull(responseFind.getBody());
	}

	@WithMockUser(username = "alvaro", authorities = { "USER" })
	@Test
	void testDeleteCommentary() throws Exception {
		Forum forum = this.forumService.findAll().get(0);
		Developer developer = this.developerService.findByUsername("alvaro");
		Commentary comment = new Commentary("I love games", new Date(), false, developer, forum);

		ResponseEntity<HttpStatus> responseFind = this.commentaryController.deletePublicationById(comment.getId());
		Assertions.assertEquals(responseFind.getStatusCodeValue(), 400);
	}
}
