package ISPP.G5.INDVELOPERS.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.models.Commentary;
import ISPP.G5.INDVELOPERS.models.Forum;
import ISPP.G5.INDVELOPERS.repositories.CommentaryRepository;
import ISPP.G5.INDVELOPERS.repositories.ForumRepository;
import io.jsonwebtoken.lang.Assert;

@Service
public class CommentaryService {
	
	@Autowired
	private CommentaryRepository repository;
	
	
	@Autowired
	private ForumService forumService;
	
	@Autowired
	private ForumRepository forumRepository;
	
	public List<Commentary> findAll(){
		return repository.findAll();
		
	}
	
	public Commentary findById(String id){
		Optional<Commentary> comment = repository.findById(id);
		if(comment.get() !=null) {
			return comment.get();
		}else {
			throw new IllegalArgumentException("This commentary doesn't exist.");
		}
	}
	
	public String addCommentary(Commentary comment, String user, String idForum) {
		Forum forum = forumService.findById(idForum);
		Date today = new Date();
		comment.setCreationDate(today);
		comment.setUsername(user);
		repository.save(comment);
		forum.getComments().add(comment);
		forumRepository.save(forum);
		return "Successfully added with id: " + comment.getId();
	}
	
	public String updateCommentary(Commentary comment, String idForum, String user) {
		Assert.notNull(comment);
		if (comment.getUsername().contentEquals(user) && !comment.getDescription().isEmpty()) {
				Forum forum = forumService.findById(idForum);
				comment.setEdited(true);
				repository.save(comment);
				forumRepository.save(forum);
		} else if (!comment.getUsername().contentEquals(user)) {
			throw new IllegalArgumentException("You don't have permissions to perform this action.");
		} else if (comment.getDescription().isEmpty()) {
			throw new IllegalArgumentException("Comment cannot be empty");
		}
		return "Updated comment with id:" + comment.getId();
	}

	public String deleteCommentary(String id, String user) {
		Optional<Commentary> optional = repository.findById(id);
		if(optional.get()!=null) {
			Commentary comment = optional.get();
			if(comment.getUsername().contentEquals(user)) {
				Optional<Forum> optionalForum = findForumByCommentId(comment.getId());
				if (optionalForum.get() != null) {
					Forum forum = optionalForum.get();
					forum.getComments().remove(comment);
					forumRepository.save(forum);
					repository.delete(comment);
			}
		}
			return "Delete comment with id: " + comment.getId();
		} else {
			throw new IllegalArgumentException("You don't have permissions to perform this action.");
		}

	}
	
	public Optional<Forum> findForumByCommentId(String id) {
		Optional<Forum> optional = Optional.empty();
		List<Forum> forums = forumService.findAll();
		for(Forum f: forums) {
			List<Commentary> comments = f.getComments();
			if(comments.size()>0) {
				for(Commentary c: comments) {
					if(c.getId().contentEquals(id)) {
						optional = Optional.of(f);
					}
				}
			}
		}
		return optional;
	}

}
