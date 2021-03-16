package ISPP.G5.INDVELOPERS.services;


import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.Security.JwtTokenProvider;
import ISPP.G5.INDVELOPERS.models.UserEntity;
import ISPP.G5.INDVELOPERS.models.UserRole;
import ISPP.G5.INDVELOPERS.repositories.UserEntityRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserEntityService {

	private JwtTokenProvider jwtTokenProvider;
	private AuthenticationManager authenticationManager;
	private UserEntityRepository userEntityRepository;

	public List<UserEntity> getAll() {
		return this.userEntityRepository.findAll();
		
	}

	public UserEntity createUser(UserEntity user) throws IllegalArgumentException{

		Assert.notNull(user);
		if(this.userEntityRepository.findByUsername(user.getUsername()).isPresent())
			throw new IllegalArgumentException("User already exists");

		if(this.userEntityRepository.findByEmail(user.getEmail()).isPresent())
			throw new IllegalArgumentException("User already exists");

		user.setPassword(new BCryptPasswordEncoder(12).encode(user.getPassword()));
		user.setRoles(Stream.of(UserRole.USER).collect(Collectors.toSet()));
		user.setEnabled(true);
		
		this.userEntityRepository.save(user);

		return user;
	}

	public UserEntity updateUser(UserEntity userToUpdate) {
		return this.userEntityRepository.save(userToUpdate);
	}

	public String login(String username, String password) throws NotFoundException{

		UserEntity user;

		try {
			authenticationManager
			.authenticate(new UsernamePasswordAuthenticationToken(username, password));

			user = this.userEntityRepository.findByUsername(username).orElseThrow(NotFoundException::new);
			return jwtTokenProvider.createToken(username, user.getId(), user.getRoles());
		} catch (AuthenticationException e) {
			throw new NotFoundException();
		}

	}

	public UserEntity findByUsername(String username) throws NotFoundException {
		
		return userEntityRepository.findByUsername(username).orElseThrow(NotFoundException::new);
	}


	public UserEntity findByEmail(String email) throws IllegalArgumentException, NotFoundException{
		Assert.hasLength(email);
		return this.userEntityRepository.findByEmail(email).orElseThrow(NotFoundException::new);
	}
	
	
}
