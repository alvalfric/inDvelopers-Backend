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

import ISPP.G5.INDVELOPERS.controllers.ForumController;
import ISPP.G5.INDVELOPERS.dtos.ForumDTO;
import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Forum;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.ForumService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ForumControllerTests {

	@Autowired
	private ForumController forumController;
	
	@Autowired
	private ForumService forumService;
	
	@Autowired
	private DeveloperService developerService;

	@Test
	void testFindAll() throws Exception {
		ResponseEntity<List<ForumDTO>> responseFind = this.forumController.findAll();
		Assertions.assertEquals(responseFind.getStatusCodeValue(), 200);
		Assertions.assertNotNull(responseFind.getBody());
		Assertions.assertEquals(responseFind.getBody().get(0).getTitle(), "Nintendo");

	}
	
	@Test
	void testFindByTtile() throws Exception {
		ResponseEntity<List<ForumDTO>> responseFind = this.forumController.findByTitle("Nintendo");
		Assertions.assertEquals(responseFind.getStatusCodeValue(), 200);
		Assertions.assertNotNull(responseFind.getBody());
		Assertions.assertEquals(responseFind.getBody().get(0).getTitle(), "Nintendo");

	}

	@WithMockUser(username = "alvaro", authorities = { "USER" })
	@Test
	void testAddForum() throws Exception {
		Developer developer = this.developerService.findByUsername("alvaro");
		Forum forum = new Forum("Ice secret is amazing!", developer, new Date());
		ResponseEntity<String> responseAdd = this.forumController.addForum(forum);
		Assertions.assertEquals(responseAdd.getStatusCodeValue(), 200);
		Assertions.assertNotNull(responseAdd.getBody());
	}
	
	@WithMockUser(username = "alvaro", authorities = { "USER" })
	@Test
	void testDeleteForum() throws Exception {
		Developer developer = this.developerService.findByUsername("alvaro");
		Forum forum = new Forum("Ice secret is amazing!", developer, new Date());
		this.forumController.addForum(forum);
		Forum findForum = this.forumService.findByTitle("Ice secret is amazing!").get(0);
		ResponseEntity<HttpStatus> responseDelete = this.forumController.deleteForumById(findForum.getId());
		Assertions.assertEquals(responseDelete.getStatusCodeValue(), 204);
	}
	
	@WithMockUser(username = "fernando", authorities = { "USER" })
	@Test
	void testDeleteForumBad() throws Exception {
		Developer developer = this.developerService.findByUsername("alvaro");
		Forum forum = new Forum("Ice secret is amazing!", developer, new Date());
		this.forumController.addForum(forum);
		Forum findForum = this.forumService.findByTitle("Ice secret is amazing!").get(0);
		ResponseEntity<HttpStatus> responseDelete = this.forumController.deleteForumById(findForum.getId());
		Assertions.assertEquals(responseDelete.getStatusCodeValue(), 403);
	}
}
