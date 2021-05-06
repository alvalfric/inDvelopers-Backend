package ISPP.G5.INDVELOPERS.mappers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import ISPP.G5.INDVELOPERS.dtos.CommentaryDTO;
import ISPP.G5.INDVELOPERS.models.Commentary;

@Component
public class CommentaryDTOConverter {

	public static CommentaryDTO convertCommentaryToCommentaryDTO(Commentary commentary) {
		return CommentaryDTO.builder().id(commentary.getId())
				.description(commentary.getDescription())
				.creationDate(commentary.getCreationDate())
				.edited(commentary.getEdited())
				.foroId(commentary.getForo().getId())
				.developerCreatorId(commentary.getDeveloper().getId())
				.developerCreatorUsername(commentary.getDeveloper().getUsername())
				.build();
	}
	
	public static List<CommentaryDTO> convertListCommentaryToListCommentaryDTO(List<Commentary> commentaries) {
		List<CommentaryDTO> res = new ArrayList<>();
		
		for(Commentary comment: commentaries) {
			CommentaryDTO commentDTO = CommentaryDTOConverter.convertCommentaryToCommentaryDTO(comment);
			res.add(commentDTO);
		}
		
		return res;
	}
}
