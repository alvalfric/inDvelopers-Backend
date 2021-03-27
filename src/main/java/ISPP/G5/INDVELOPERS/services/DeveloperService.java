package ISPP.G5.INDVELOPERS.services;


import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.Security.JwtTokenProvider;
import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.Game;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.repositories.DeveloperRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DeveloperService {

	private JwtTokenProvider jwtTokenProvider;
	private AuthenticationManager authenticationManager;
	private DeveloperRepository developerRepository;

	public List<Developer> getAll() {
		return this.developerRepository.findAll();
		
	}

	public Developer createDeveloper(Developer developer) throws IllegalArgumentException{

		Assert.notNull(developer);
		if(this.developerRepository.findByUsername(developer.getUsername()).isPresent())
			throw new IllegalArgumentException("Developer already exists");

		if(this.developerRepository.findByEmail(developer.getEmail()).isPresent())
			throw new IllegalArgumentException("Developer already exists");

		developer.setPassword(new BCryptPasswordEncoder(12).encode(developer.getPassword()));
		developer.setRoles(Stream.of(UserRole.USER).collect(Collectors.toSet()));
		

		
		this.developerRepository.save(developer);

		return developer;
	}

	public Developer updateDeveloper(Developer developerToUpdate) {
		return this.developerRepository.save(developerToUpdate);
	}

	public String login(String username, String password) throws NotFoundException{

		Developer developer;

		try {
			authenticationManager
			.authenticate(new UsernamePasswordAuthenticationToken(username, password));

			developer = this.developerRepository.findByUsername(username).orElseThrow(NotFoundException::new);
			return jwtTokenProvider.createToken(username, developer.getId(), developer.getRoles());
		} catch (AuthenticationException e) {
			throw new NotFoundException();
		}

	}

	public Developer findByUsername(String username) throws NotFoundException {
		
		return developerRepository.findByUsername(username).orElseThrow(NotFoundException::new);
	}


	public Developer findByEmail(String email) throws IllegalArgumentException, NotFoundException{
		Assert.hasLength(email);
		return this.developerRepository.findByEmail(email).orElseThrow(NotFoundException::new);
	}
	
	public Developer findById(String id) throws NotFoundException {
		return this.developerRepository.findById(id).orElseThrow(NotFoundException::new);
	}
	
	public void deleteDeveloper(String id) throws NotFoundException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Developer admin = this.developerRepository.findByUsername(userDetails.getUsername()).orElseThrow(NotFoundException::new);
		if (!admin.getRoles().contains(UserRole.ADMIN)) { 
			throw new IllegalArgumentException("Only the admin can remove a developer");
		} else {
			this.developerRepository.deleteById(id);
		}
	}
	
	
}
