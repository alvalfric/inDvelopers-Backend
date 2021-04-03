package ISPP.G5.INDVELOPERS.services;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
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
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.repositories.DeveloperRepository;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DeveloperService {

	private JwtTokenProvider jwtTokenProvider;
	private AuthenticationManager authenticationManager;
	private DeveloperRepository developerRepository;

	public List<Developer> getAll() {
		return this.developerRepository.findAll();
		
	}

	public Developer createDeveloper(Developer developer) {

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

	public String login(String username, String password) {

		Developer developer;

		try {
			authenticationManager
			.authenticate(new UsernamePasswordAuthenticationToken(username, password));

			developer = this.developerRepository.findByUsername(username).orElse(null);
			return jwtTokenProvider.createToken(username, developer.getId(), developer.getRoles());
		} catch (AuthenticationException e) {
			throw new IllegalArgumentException();
		}

	}

	public Developer findByUsername(String username) {
		
		return developerRepository.findByUsername(username).orElse(null);
	}


	public Developer findByEmail(String email){
		Assert.hasLength(email);
		return this.developerRepository.findByEmail(email).orElse(null);
	}
	
	public Developer findById(String id)  {
		return this.developerRepository.findById(id).orElse(null);
	}
	
	public void deleteDeveloper(String id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Developer admin = this.developerRepository.findByUsername(userDetails.getUsername()).orElse(null);
		if (!admin.getRoles().contains(UserRole.ADMIN)) { 
			throw new IllegalArgumentException("Only the admin can remove a developer");
		} else {
			this.developerRepository.deleteById(id);
		}
	}
	
	public Developer findCurrentDeveloper() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Developer developer = findByUsername(userDetails.getUsername());
		return developer;
	}
}
