package ISPP.G5.INDVELOPERS.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartFile.*;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Publication;
import ISPP.G5.INDVELOPERS.services.DeveloperService;
import ISPP.G5.INDVELOPERS.services.PublicationService;

@RestController
@CrossOrigin("*")
@RequestMapping("/publications")
public class PublicationController {

	@Autowired
	private PublicationService publicationService;

	@Autowired
	private DeveloperService developService;

	@GetMapping("/findAll")
	public List<Publication> getPublications() {
		return this.publicationService.findAll();

	}

	@GetMapping("/findByName")
	public ResponseEntity<List<Publication>> getPublicationsByUsername() throws NotFoundException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		String username = userDetails.getUsername();

		try {
			return ResponseEntity.ok(this.publicationService.findByUSername(username));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/findById/{id}")
	public ResponseEntity<Publication> getPublicationById(@PathVariable String id) {
		try {
			return ResponseEntity.ok(this.publicationService.findById(id));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@PostMapping("/add")
	public String addPublication(@RequestBody Publication publication, @RequestParam("file") MultipartFile imagen) throws NotFoundException, IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		String user = userDetails.getUsername();

		if(!imagen.isEmpty()) {
			Path directorio = Paths.get("src//main//resources//static/images");
			String rutaAbsoluta = directorio.toFile().getAbsolutePath();
			try {
				byte[] bytesImg = imagen.getBytes();
				Path rutaCompleta = Paths.get(rutaAbsoluta + "//"+ imagen.getOriginalFilename());
				Files.write(rutaCompleta, bytesImg);
				publication.setImagen2(imagen.getOriginalFilename());
			}catch(IOException e) {
				e.printStackTrace();
			}
			
			
		
		}
		try {
			Developer developer = this.developService.findByUsername(user);
			return this.publicationService.addPublication(publication, developer);
		} catch (NotFoundException e) {
			throw new IllegalArgumentException("Publication couldn't be created correctly.");
		}

	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deletePublicationById(@PathVariable String id) throws NotFoundException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		String user = userDetails.getUsername();

		try {
			Developer developer = this.developService.findByUsername(user);
			Publication p = this.publicationService.findById(id);
			this.publicationService.deletePublication(p, developer);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("You don't have permissions to perform this action.");
		}

	}

}
