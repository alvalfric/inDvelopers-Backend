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

import ISPP.G5.INDVELOPERS.models.Commentary;
import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Forum;
import ISPP.G5.INDVELOPERS.services.CommentaryService;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.ForumService;

@SpringBootTest
public class CommentaryServiceTest {

	@Autowired
	private CommentaryService commentaryService;

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
		List<Commentary> allCommentaries = this.commentaryService.findAll();
		assertThat(allCommentaries.get(0).getDescription()).isEqualTo("I'm looking for video games for nintendo");
	}

	@Test
	void shouldFindById() {
		List<Commentary> allCommentaries = this.commentaryService.findAll();
		Commentary comment = this.commentaryService.findById(allCommentaries.get(0).getId());
		assertThat(comment.getDescription()).isEqualTo(allCommentaries.get(0).getDescription());
	}

	@Test
	void shouldNotFindById() {
		String commentId = "errorId";
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.commentaryService.findById(commentId);
		});
	}

	@Test
	void shouldFindByForum() {
		List<Forum> allForums = this.forumService.findByTitle("Nintendo");
		List<Commentary> commentsByForum = this.commentaryService.findByForum(allForums.get(0).getId());
		Commentary comment = this.commentaryService.findById(commentsByForum.get(0).getId());

		assertThat(commentsByForum.get(0)).isEqualTo(comment);
	}

	@Test
	void shouldAddCommentary() {
		Developer developer = this.developerService.findByUsername("alvaro");
		Forum forum = this.forumService.findByTitle("Nintendo").get(0);
		Commentary comment = new Commentary("Love nintendo games!", new Date(), false, developer, forum);
		this.commentaryService.addCommentary(comment, forum.getId(), developer);

		List<Commentary> commentsByForum = this.commentaryService.findByForum(forum.getId());
		assertThat(commentsByForum.get(2).getDescription()).isEqualTo("Love nintendo games!");
	}

	@Test
	void shouldUpdateCommentary() {
		Developer developer = this.developerService.findByUsername("alvaro");
		Forum forum = this.forumService.findByTitle("Nintendo").get(0);
		List<Commentary> commentsByForum = this.commentaryService.findByForum(forum.getId());
		Commentary comment = this.commentaryService.findById(commentsByForum.get(1).getId());
		comment.setEdited(true);

		assertThat(this.commentaryService.updateCommentary(comment.getId(), comment, developer))
				.isEqualTo("Updated comment with id:" + comment.getId());
	}

	@Test
	void shouldNotUpdateCommentaryWrongUser() {
		Developer developer = this.developerService.findByUsername("fernando");
		Forum forum = this.forumService.findByTitle("Nintendo").get(0);
		List<Commentary> commentsByForum = this.commentaryService.findByForum(forum.getId());
		Commentary comment = this.commentaryService.findById(commentsByForum.get(1).getId());
		comment.setEdited(true);

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.commentaryService.updateCommentary(comment.getId(), comment, developer);
		});
	}

	@Test
	void shouldNotUpdateCommentaryEmptyDescription() {
		Developer developer = this.developerService.findByUsername("alvaro");
		Forum forum = this.forumService.findByTitle("Nintendo").get(0);
		List<Commentary> commentsByForum = this.commentaryService.findByForum(forum.getId());
		Commentary comment = this.commentaryService.findById(commentsByForum.get(1).getId());
		comment.setEdited(true);
		comment.setDescription("");

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.commentaryService.updateCommentary(comment.getId(), comment, developer);
		});
	}

	@Test
	void shouldDeleteCommentary() {
		Developer developer = this.developerService.findByUsername("alvaro");
		Forum forum = this.forumService.findByTitle("Nintendo").get(0);
		List<Commentary> commentsByForum = this.commentaryService.findByForum(forum.getId());
		Commentary comment = this.commentaryService.findById(commentsByForum.get(1).getId());
		comment.setEdited(true);

		assertThat(this.commentaryService.deleteCommentary(comment.getId(), developer))
				.isEqualTo("Delete comment with id: " + comment.getId());
	}

	@Test
	void shouldNotDeleteCommentary() {
		Developer developer = this.developerService.findByUsername("fernando");
		Forum forum = this.forumService.findByTitle("Nintendo").get(0);
		List<Commentary> commentsByForum = this.commentaryService.findByForum(forum.getId());
		Commentary comment = this.commentaryService.findById(commentsByForum.get(1).getId());
		comment.setEdited(true);

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.commentaryService.deleteCommentary(comment.getId(), developer);
		});
	}
}
