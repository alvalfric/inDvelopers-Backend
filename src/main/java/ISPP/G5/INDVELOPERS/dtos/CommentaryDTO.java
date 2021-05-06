package ISPP.G5.INDVELOPERS.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentaryDTO {

	private String id;
	
	private String description;
	
	private Date creationDate;
	
	private Boolean edited;
	
	private String foroId;

	private String developerCreatorId;
	
	private String developerCreatorUsername;
}
