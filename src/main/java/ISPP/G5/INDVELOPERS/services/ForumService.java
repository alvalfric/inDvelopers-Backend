package ISPP.G5.INDVELOPERS.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Forum;
import ISPP.G5.INDVELOPERS.repositories.ForumRepository;

@Service
public class ForumService {

	
	@Autowired
	private ForumRepository repository;
	
	public List<Forum> findAll(){
		return repository.findAll();
	}
	
	public Forum findById(String id){
		Optional<Forum> forum = repository.findById(id);
		if(forum.isPresent()) {
			return forum.get();
		}else {
			throw new IllegalArgumentException("This forum doesn't exist.");
		}
	}
	
	public List<Forum> findByTitle(String title){
//		List<Forum> forums = repository.findAll();
//		List<Forum> res = new ArrayList<Forum>();
//		for(Forum f: forums) {
//			if(f.getTitle().toLowerCase().contains(title.toLowerCase())) {
//				res.add(f);
//			}
//		}
		List<Forum> res = repository.findByTitle(title);
		return res;
	}
	
	public String addForum(Forum forum, Developer developer) {
		Date today = new Date();
		forum.setDeveloper(developer);
		forum.setCreationDate(today);
		repository.save(forum);
		return "Successfully added with id: " + forum.getId();
	}
	
	public String deleteForum(Forum forum, Developer developer) {
		if (forum.getDeveloper().getUsername().contentEquals(developer.getUsername())) {
			repository.delete(forum);
			return "Delete forum with id: " + forum.getId();
		} else {
			return "You don't have permissions to perform this action";
		}

	}
	
}
