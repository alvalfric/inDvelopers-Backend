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
public class ForumDTO {

	private String id;
	
	private String title;
	
	private Date creationDate;

	private String developerCreatorId;
	
	private String developerCreatorUsername;
	
}
