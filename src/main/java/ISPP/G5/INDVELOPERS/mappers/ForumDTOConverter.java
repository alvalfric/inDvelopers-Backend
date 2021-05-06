package ISPP.G5.INDVELOPERS.mappers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import ISPP.G5.INDVELOPERS.dtos.ForumDTO;
import ISPP.G5.INDVELOPERS.models.Forum;

@Component
public class ForumDTOConverter {

	public static ForumDTO convertForumToForumDTO(Forum forum) {
		return ForumDTO.builder().id(forum.getId())
				.title(forum.getTitle())
				.creationDate(forum.getCreationDate())
				.developerCreatorId(forum.getDeveloper().getId())
				.developerCreatorUsername(forum.getDeveloper().getUsername())
				.build();
	}
	
	public static List<ForumDTO> convertListForumToListForumDTO(List<Forum> forums) {
		List<ForumDTO> res = new ArrayList<>();
		
		for(Forum forum: forums) {
			ForumDTO forumDTO = ForumDTOConverter.convertForumToForumDTO(forum);
			res.add(forumDTO);
		}
		
		return res;
	}
}
