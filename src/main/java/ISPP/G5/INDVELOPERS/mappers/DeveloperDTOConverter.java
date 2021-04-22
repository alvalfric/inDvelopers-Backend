package ISPP.G5.INDVELOPERS.mappers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import ISPP.G5.INDVELOPERS.dtos.GetDeveloperDTO;
import ISPP.G5.INDVELOPERS.models.Developer;

@Component
public class DeveloperDTOConverter {

	
	public static GetDeveloperDTO DevelopertoGetDeveloperDTO(Developer developer) {
		
		List<GetDeveloperDTO> followingDTODevelopers = new ArrayList<GetDeveloperDTO>();
		
		for(Developer dev: developer.getFollowing()) {
			GetDeveloperDTO followingDev = GetDeveloperDTO.builder().id(dev.getId())
			.username(dev.getUsername())
			.email(dev.getEmail())
			.userImage(dev.getUserImage())
			.roles(dev.getRoles())
			.description(dev.getDescription())
			.technologies(dev.getTechnologies())
			.isPremium(dev.getIsPremium())
			.build();
			
			followingDTODevelopers.add(followingDev);
		}
		
		return GetDeveloperDTO.builder().id(developer.getId())
				.username(developer.getUsername())
				.email(developer.getEmail())
				.userImage(developer.getUserImage())
				.roles(developer.getRoles())
				.description(developer.getDescription())
				.technologies(developer.getTechnologies())
				.isPremium(developer.getIsPremium())
				.following(followingDTODevelopers)
				.build();
	}
}
