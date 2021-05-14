package ISPP.G5.INDVELOPERS.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.models.Commentary;
import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Forum;
import ISPP.G5.INDVELOPERS.repositories.CommentaryRepository;
import io.jsonwebtoken.lang.Assert;

@Service
public class CommentaryService {

	@Autowired
	private CommentaryRepository repository;

	@Autowired
	private ForumService forumService;

	public List<Commentary> findAll() {
		return repository.findAll();

	}

	public Commentary findById(String id) {
		Optional<Commentary> comment = repository.findById(id);
		if (comment.isPresent()) {
			return comment.get();
		} else {
			throw new IllegalArgumentException("This commentary doesn't exist.");
		}
	}

	public List<Commentary> findByForum(String id) {
//		List<Commentary> comments = repository.findAll();
//		List<Commentary> res = new ArrayList<Commentary>();
//		for (Commentary c : comments) {
//			if (c.getForo().getId().contentEquals(id)) {
//				res.add(c);
//			}
//		}
//		if (res.size() > 0) {
//			return res;
//		} else {
//			throw new IllegalArgumentException("There are no comments in this forum yet.");
//		}
		List<Commentary> res = repository.findByForumId(id);
		return res;
	}

	public String addCommentary(Commentary comment, String idForum, Developer developer) {
		Forum forum = forumService.findById(idForum);
		Date today = new Date();
		comment.setCreationDate(today);
		comment.setDeveloper(developer);
		comment.setForo(forum);
		comment.setEdited(false);
		repository.save(comment);
		return "Successfully added with id: " + comment.getId();
	}

	public String updateCommentary(String id, Commentary comment, Developer developer) {
		Assert.notNull(comment);
		Commentary commentary = repository.findById(id).get();
		if (commentary.getDeveloper().getUsername().contentEquals(developer.getUsername())
				&& !comment.getDescription().isEmpty()) {
			commentary.setEdited(true);
			commentary.setDescription(comment.getDescription());
			repository.save(commentary);
		} else if (!comment.getDeveloper().getUsername().contentEquals(developer.getUsername())) {
			throw new IllegalArgumentException("You don't have permissions to perform this action.");
		} else if (comment.getDescription().isEmpty()) {
			throw new IllegalArgumentException("Comment cannot be empty");
		}
		return "Updated comment with id:" + commentary.getId();
	}

	public String deleteCommentary(String id, Developer developer) {
		Commentary comment = repository.findById(id).get();
		if (comment.getDeveloper().getUsername().contentEquals(developer.getUsername())) {
			comment.setForo(null);
			comment.setDeveloper(null);
			repository.delete(comment);
			return "Delete comment with id: " + comment.getId();
		} else {
			throw new IllegalArgumentException("You don't have permissions to perform this action.");
		}
	}

}
