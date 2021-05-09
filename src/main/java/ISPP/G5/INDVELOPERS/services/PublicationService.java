package ISPP.G5.INDVELOPERS.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.Publication;
import ISPP.G5.INDVELOPERS.repositories.PublicationRepository;
import io.jsonwebtoken.lang.Assert;


@Service
public class PublicationService {

	@Autowired
	private PublicationRepository publicationRepository;
	

	public List<Publication> findAll() {
		List<Publication> res = new ArrayList<>();
		res = publicationRepository.findAll();
		Collections.reverse(res);
		return res;
	}

	public Publication findById(String id) {
		Optional<Publication> publication = this.publicationRepository.findById(id);
		if (publication.get() != null) {
			return publication.get();
		} else {
			throw new IllegalArgumentException("This publication doesn't exist.");
		}

	}

	public List<Publication> findByUSername(String username) {
		List<Publication> allPublications = this.publicationRepository.findAll();
		List<Publication> publications = new ArrayList<Publication>();
		for (Publication p : allPublications) {
			if (p.getUsername().contentEquals(username)) {
				publications.add(p);
			}
		}
		Collections.reverse(publications);
		return publications;
	}

	public String addPublication(Publication p, Developer d) {
		p.setDeveloper(d);
		this.publicationRepository.save(p);
		return "Successfully added with id: " + p.getId();
	}
	
	public String updatePublication(Publication publication) {
		Assert.notNull(publication);
		this.publicationRepository.save(publication);
		return "Updated publication with id:" + publication.getId();
	}

	public String deletePublication(Publication p, Developer developer) {
		if (developer.getUsername().contentEquals(p.getUsername())) {
			p.setDeveloper(null);
			this.publicationRepository.delete(p);
			return "Delete publication with id: " + p.getId();
		} else {
			throw new IllegalArgumentException("You don't have permissions to perform this action.");
		}

	}
	
	public List<Publication> publicationsByFollowedDevelopers(Developer developer) {

		List<Publication> res = new ArrayList<>();
		List<Developer> followed = developer.getFollowing();
		for (Developer d : followed) {
			res.addAll(publicationRepository.findByDeveloper(d.getId()));
		}
		return res;

	}
	

}