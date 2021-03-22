package ISPP.G5.INDVELOPERS.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public List<Publication> findByUSername(String username) {
		return this.publicationRepository.findByUsername(username);
	}

	public String addPublication(Publication p) {
			this.publicationRepository.save(p);
			return "Successfully added with id: " + p.getId();
	}

	public String deletePublication(Publication p) {
		this.publicationRepository.delete(p);
		return "Delete publication with id: " + p.getId();
	}

	public Publication updatePublication(Publication p) {
		return this.publicationRepository.save(p);
	}

}
