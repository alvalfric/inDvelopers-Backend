package ISPP.G5.INDVELOPERS.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Forum;
import ISPP.G5.INDVELOPERS.repositories.ForumRepository;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.ForumService;

@SpringBootTest
public class ForumServiceTests {

	@Autowired
	private ForumRepository forumRepository;

	@Autowired
	private ForumService forumService;

	@Autowired
	private DeveloperService developerService;
	
	@BeforeEach
	void initAll() throws NotFoundException {

	}

	@AfterEach
	void end() {

	}

	@Test
	void shouldFindAll() {
		List<Forum> allForums = this.forumService.findAll();
		assertThat(allForums.get(0).getTitle()).isEqualTo("Nintendo");
	}

	@Test
	void shouldFindById() {
		List<Forum> allForums = this.forumService.findAll();
		String forumId = allForums.get(0).getId();
		Forum forum = this.forumService.findById(forumId);
		assertThat(forum).isEqualTo(allForums.get(0));
	}

	@Test
	void shouldNotFindById() {
		String forumId = "errorId";
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.forumService.findById(forumId);
		});
	}
	
	@Test
	void shouldFindByTitle() {
		List<Forum> allForums = this.forumService.findByTitle("Nintendo");
		assertThat(allForums.get(0).getTitle()).isEqualTo("Nintendo");
	}

	@Test
	void shouldAddForum() {
		Developer developer = this.developerService.findByUsername("alvaro");
		Forum forum = new Forum("Ice secret is amazing!", developer, new Date());
		this.forumService.addForum(forum, developer);
		
		List<Forum> allForums = this.forumService.findAll();
		assertThat(allForums.get(2).getTitle()).isEqualTo("Ice secret is amazing!");
	}
	
	@Test
	void shouldDeleteForum() {
		Developer developer = this.developerService.findByUsername("alvaro");
		this.forumService.addForum(new Forum("Ice secret is amazing!", developer, new Date()), developer);
		Forum forum = this.forumService.findByTitle("Ice secret is amazing!").get(0);
		
		assertThat(this.forumService.deleteForum(forum, developer)).isEqualTo("Delete forum with id: " + forum.getId());
	}
	
	@Test
	void shouldNotDeleteForum() {
		Developer developer = this.developerService.findByUsername("fernando");
		Forum forum = this.forumService.findByTitle("Nintendo").get(0);
		
		assertThat(this.forumService.deleteForum(forum, developer)).isEqualTo("You don't have permissions to perform this action");
	}
}
