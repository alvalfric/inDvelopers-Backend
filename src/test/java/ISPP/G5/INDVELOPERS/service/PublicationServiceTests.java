package ISPP.G5.INDVELOPERS.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Publication;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.repositories.DeveloperRepository;
import ISPP.G5.INDVELOPERS.repositories.PublicationRepository;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.PublicationService;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

@SpringBootTest
public class PublicationServiceTests {

	@Autowired
	protected PublicationService publicationService;

	@Autowired
	protected DeveloperService developerService;

	@Autowired
	protected DeveloperRepository developerRepository;

	@Autowired
	protected PublicationRepository publicationRepository;

	@BeforeEach
	void initAll() throws NotFoundException {
		Set<UserRole> roles = new HashSet<UserRole>();
		roles.add(UserRole.USER);
		Developer developer1 = new Developer("martaad", "Marta123", "maartaadq@alum.us.es", null, null, roles,
				"I'm developer", "technologies", true);
		Developer developer2 = new Developer("miguel001", "Miguel1234", "maartadq11@alum.us.es", null, null, roles,
				"I'm developer", "technologies", true);
		this.developerService.createDeveloper(developer1);
		this.developerService.createDeveloper(developer2);
		Publication publication = new Publication("martaad", null, "description of publication", null, developer1);
		this.publicationService.addPublication(publication, developer1);
	}

	@AfterEach
	void end() {
		developerRepository.deleteAll();
		publicationRepository.deleteAll();
	}

	@Test
	void findPublicationByID() {
		Publication publication = this.publicationRepository.findByUsername("martaad").get(0);
		String id = publication.getId();
		Publication p = this.publicationService.findById(id);
		Assertions.assertTrue(p.equals(publication));
	}

	@Test
	void findPublicationNotExist() {

		Assertions.assertThrows(NoSuchElementException.class, () -> {
			publicationService.findById("123asx");
		});
	}

	@Test
	void deletePublicationSuccess() throws NotFoundException {

		Publication publication = this.publicationRepository.findByUsername("martaad").get(0);
		Developer developer = this.developerService.findByUsername("martaad");

		List<Publication> publications = this.publicationService.findAll();
		Assertions.assertTrue(publications.contains(publication));

		this.publicationService.deletePublication(publication, developer);
		publications.removeAll(publications);
		publications.addAll(this.publicationService.findAll());
		Assertions.assertTrue(!publications.contains(publication));
	}

	@Test
	void deletePublicationNotSuccess() throws NotFoundException {

		Publication publication = this.publicationRepository.findByUsername("martaad").get(0);
		Developer developer = this.developerService.findByUsername("miguel001");
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			publicationService.deletePublication(publication, developer);
		});

	}

	@Test
	void findPublicationsByNameSuccess() throws NotFoundException {

		Developer martaad = this.developerService.findByUsername("martaad");
		Assertions.assertTrue(this.developerRepository.findAll().contains(martaad));

		List<Publication> publications = this.publicationService.findByUSername("martaad");
		for (Publication p : publications) {
			Assertions.assertTrue(p.getUsername().contentEquals("martaad"));
		}

	}

	@Test
	void addPublication() throws NotFoundException {
		Developer martaad = this.developerService.findByUsername("martaad");
		Publication publication = new Publication("martaad", null, "description of publication", null, martaad);
		this.publicationService.addPublication(publication, martaad);
		Assertions.assertTrue(this.publicationService.findAll().contains(publication));
	}

}
