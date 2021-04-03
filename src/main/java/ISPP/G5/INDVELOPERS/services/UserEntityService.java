
package ISPP.G5.INDVELOPERS.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserEntityService {

	private JwtTokenProvider		jwtTokenProvider;
	private AuthenticationManager	authenticationManager;
	private UserEntityRepository	userEntityRepository;


	public List<UserEntity> getAll() {
		return userEntityRepository.findAll();

	}

	public UserEntity createUser(final UserEntity user) throws IllegalArgumentException {

		Assert.notNull(user);
		if (userEntityRepository.findByUsername(user.getUsername()).isPresent())
			throw new IllegalArgumentException("User already exists");

		if (userEntityRepository.findByEmail(user.getEmail()).isPresent())
			throw new IllegalArgumentException("User already exists");

		user.setPassword(new BCryptPasswordEncoder(12).encode(user.getPassword()));
		user.setRoles(Stream.of(UserRole.USER).collect(Collectors.toSet()));
		user.setEnabled(true);

		userEntityRepository.save(user);

		return user;
	}

	public UserEntity updateUser(final UserEntity userToUpdate) {
		return userEntityRepository.save(userToUpdate);
	}

	public String login(final String username, final String password) throws NotFoundException {

		UserEntity user;

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

			user = userEntityRepository.findByUsername(username).orElse(null);
			return jwtTokenProvider.createToken(username, user.getId(), user.getRoles());
		} catch (AuthenticationException e) {
			throw new IllegalArgumentException("Excepción de autenticación");
		}

	}

	public UserEntity findByUsername(final String username) {

		return userEntityRepository.findByUsername(username).orElse(null);
	}

	public UserEntity findByEmail(final String email) throws IllegalArgumentException {
		Assert.hasLength(email);
		return userEntityRepository.findByEmail(email).orElse(null);
	}

}
