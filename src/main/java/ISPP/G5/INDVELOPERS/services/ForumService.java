package ISPP.G5.INDVELOPERS.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.models.Commentary;
import ISPP.G5.INDVELOPERS.models.Forum;
import ISPP.G5.INDVELOPERS.repositories.CommentaryRepository;
import ISPP.G5.INDVELOPERS.repositories.ForumRepository;

@Service
public class ForumService {

	
	@Autowired
	private ForumRepository repository;
	
	@Autowired
	private CommentaryRepository commentaryRepository;
	
	public List<Forum> findAll(){
		return repository.findAll();
		
	}
	
	public Forum findById(String id){
		Optional<Forum> forum = repository.findById(id);
		if(forum.get() !=null) {
			return forum.get();
		}else {
			throw new IllegalArgumentException("This forum doesn't exist.");
		}
	}
	
	public List<Forum> findByTitle(String title){
		List<Forum> forums = repository.findAll();
		List<Forum> res = new ArrayList<Forum>();
		for(Forum f: forums) {
			if(f.getTitle().contains(title)) {
				res.add(f);
			}
		}
		return res;
	}
	
	public String addForum(Forum f, String user) {
		Date today = new Date();
		f.setUsername(user);
		f.setCreationDate(today);
		repository.save(f);
		return "Successfully added with id: " + f.getId();
	}
	
	public String deleteForum(Forum forum, String user) {
		if (forum.getUsername().contentEquals(user)) {
			List<Commentary> comments = forum.getComments();
			forum.setComments(null);
			if(comments.size()>0) {
				for(Commentary c: comments) {
					commentaryRepository.delete(c);
				}
			}
			repository.delete(forum);
			return "Delete forum with id: " + forum.getId();
		} else {
			throw new IllegalArgumentException("You don't have permissions to perform this action.");
		}

	}
	
}
