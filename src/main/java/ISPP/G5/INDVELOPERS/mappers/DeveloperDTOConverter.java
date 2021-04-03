package ISPP.G5.INDVELOPERS.mappers;

import org.springframework.stereotype.Component;

import ISPP.G5.INDVELOPERS.dtos.GetDeveloperDTO;
import ISPP.G5.INDVELOPERS.models.Developer;

@Component
public class DeveloperDTOConverter {

	
	public static GetDeveloperDTO DevelopertoGetDeveloperDTO(Developer developer) {
		
		return GetDeveloperDTO.builder().id(developer.getId())
				.username(developer.getUsername())
				.email(developer.getEmail())
				.gameList(developer.getGameList())
				.userImage(developer.getUserImage())
				.roles(developer.getRoles())
				.description(developer.getDescription())
				.technologies(developer.getTechnologies())
				.isPremium(developer.getIsPremium()).build();
	}
}
