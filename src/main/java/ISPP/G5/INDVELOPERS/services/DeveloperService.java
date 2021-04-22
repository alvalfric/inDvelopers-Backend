package ISPP.G5.INDVELOPERS.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
import ISPP.G5.INDVELOPERS.dtos.GetDeveloperDTO;
import ISPP.G5.INDVELOPERS.mappers.DeveloperDTOConverter;
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
		if (this.developerRepository.findByUsername(developer.getUsername()).isPresent())
			throw new IllegalArgumentException("Developer already exists");

		if (this.developerRepository.findByEmail(developer.getEmail()).isPresent())
			throw new IllegalArgumentException("Developer already exists");

		developer.setPassword(new BCryptPasswordEncoder(12).encode(developer.getPassword()));
		developer.setRoles(Stream.of(UserRole.USER).collect(Collectors.toSet()));
		this.developerRepository.save(developer);

		return developer;
	}

	public Developer changeToAdmin(String id) {
		Developer developer = this.developerRepository.findById(id).orElse(null);
		if (developer == null) {
			throw new IllegalArgumentException("Developer does not exist");
		}
		if (developer.getRoles().contains(UserRole.ADMIN)) {
			throw new IllegalArgumentException("The user is already an admin");
		}
		Set<UserRole> roles = developer.getRoles();
		roles.add(UserRole.ADMIN);
		developer.setRoles(roles);
		this.developerRepository.save(developer);
		return developer;
	}

	public Developer changeToUser(String id) {
		Developer developer = this.developerRepository.findById(id).orElse(null);
		if (developer == null) {
			throw new IllegalArgumentException("Developer does not exist");
		}
		if (!developer.getRoles().contains(UserRole.ADMIN)) {
			throw new IllegalArgumentException("The user you are trying to modify is not an admin");
		}
		Set<UserRole> roles = developer.getRoles();
		roles.remove(UserRole.ADMIN);
		developer.setRoles(roles);
		this.developerRepository.save(developer);
		return developer;
	}

	public Developer updateDeveloper(Developer developerToUpdate) {
		return this.developerRepository.save(developerToUpdate);
	}

	public String login(String username, String password) {

		Developer developer;

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

			developer = this.developerRepository.findByUsername(username).orElse(null);
			this.updateDeveloper(developer);

			return jwtTokenProvider.createToken(username, developer.getId(), developer.getRoles());
		} catch (AuthenticationException e) {
			throw new IllegalArgumentException();
		}

	}

	public Developer findByUsername(String username) {
		return developerRepository.findByUsername(username).orElse(null);
	}

	public Developer findByEmail(String email) {
		Assert.hasLength(email);
		return this.developerRepository.findByEmail(email).orElse(null);
	}

	public Developer findById(String id) {
		return this.developerRepository.findById(id).orElse(null);
	}

	public String deleteDeveloper(String toDeleteDeveloperId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Developer admin = this.developerRepository.findByUsername(userDetails.getUsername()).orElse(null);
		Developer toDeleteDeveloper = this.findById(toDeleteDeveloperId);

		if (toDeleteDeveloper != null) {
			if (admin.equals(toDeleteDeveloper)) {
				return "You cannot remove yourself!";
			}
			if (!admin.getRoles().contains(UserRole.ADMIN)) {
				return "Only administrators can remove developers";
			} else {
				String result = "Developer with username " + this.findById(toDeleteDeveloperId).getUsername()
						+ " has been removed sucessfully";
				this.developerRepository.deleteById(toDeleteDeveloperId);
				return result;
			}
		} else {
			return "Developer not found";

		}
	}

	public Developer findCurrentDeveloper() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Developer developer = findByUsername(userDetails.getUsername());
		return developer;
	}

	/* Following users implementation */

	public String followDeveloper(String username) {
		Developer currentDeveloper = this.findCurrentDeveloper();
		Developer devToFollow = this.findByUsername(username);
		if (devToFollow == null) {
			return "This developer doesn't exist";
		} else if (currentDeveloper.getFollowing().contains(devToFollow)) {
			return "You are already following this user";
		} else if (devToFollow.equals(currentDeveloper)) {
			return "You can't follow yourself";
		} else {
			currentDeveloper.getFollowing().add(devToFollow);
			this.updateDeveloper(currentDeveloper);
			return "You are now following " + devToFollow.getUsername();
		}
	}

	public String unfollowDeveloper(String username) {
		Developer currentDeveloper = this.findCurrentDeveloper();
		Developer devToUnfollow = this.findByUsername(username);
		if (devToUnfollow == null) {
			return "This developer doesn't exist";
		} else if (currentDeveloper.getFollowing().contains(devToUnfollow)) {
			currentDeveloper.getFollowing().remove(devToUnfollow);
			this.updateDeveloper(currentDeveloper);
			return "You are not following " + devToUnfollow.getUsername() + " anymore";
		} else {
			return "You are not following " + devToUnfollow.getUsername();
		}
	}

	public List<Developer> getMyFollowers(String username) {
		Developer currentDeveloper = this.findByUsername(username);
		List<Developer> myFollowers = new ArrayList<Developer>();

		myFollowers.addAll(this.developerRepository.findMyFollowers(currentDeveloper.getId()));

		return myFollowers;
	}

	public List<GetDeveloperDTO> getMyFollowersDTO(String username) {
		List<Developer> myFollowers = this.getMyFollowers(username);
		List<GetDeveloperDTO> myFollowersDTO = new ArrayList<GetDeveloperDTO>();

		for (Developer dev : myFollowers) {
			GetDeveloperDTO devToAdd = DeveloperDTOConverter.DevelopertoGetDeveloperDTO(dev);
			myFollowersDTO.add(devToAdd);
		}

		return myFollowersDTO;
	}
	
	public List<GetDeveloperDTO> getMyFollowedDTO(String username) {
		Developer currentDeveloper = this.findByUsername(username);
		List<GetDeveloperDTO> myFollowersDTO = new ArrayList<GetDeveloperDTO>();

		for (Developer dev : currentDeveloper.getFollowing()) {
			GetDeveloperDTO devToAdd = DeveloperDTOConverter.DevelopertoGetDeveloperDTO(dev);
			myFollowersDTO.add(devToAdd);
		}

		return myFollowersDTO;
	}
}
