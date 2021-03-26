package ISPP.G5.INDVELOPERS.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Publication;
import ISPP.G5.INDVELOPERS.repositories.PublicationRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PublicationService {

	private PublicationRepository publicationRepository;

	public List<Publication> findAll() {
		return publicationRepository.findAll();
	}

	public Publication findById(String id) {
		Optional<Publication> publication = this.publicationRepository.findById(id);
		if (publication.get() != null) {
			return publication.get();

		} else {
			throw new IllegalArgumentException("This publication doesn't exist.");
		}

	}

	public List<Publication> findByUSername() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		String usuario = userDetails.getUsername();

		List<Publication> allPublications = this.publicationRepository.findAll();
		List<Publication> publications = new ArrayList<Publication>();
		for (Publication p : allPublications) {
			if (p.getUsername().contentEquals(usuario)) {
				publications.add(p);
			}
		}
		return publications;
	}

	public String addPublication(Publication p, Developer d) {
		Optional<Publication> publication = this.publicationRepository.findById(p.getId());
		publication.get().setDeveloper(d);
		this.publicationRepository.save(p);
		return "Successfully added with id: " + p.getId();
	}

	public String deletePublication(Publication p) {
		p.setDeveloper(null);
		this.publicationRepository.delete(p);
		return "Delete publication with id: " + p.getId();
	}

	public Publication updatePublication(Publication p) {
		return this.publicationRepository.save(p);
	}

}
