package ISPP.G5.INDVELOPERS.models;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document(collection = "Commentary")
@AllArgsConstructor
public class Commentary extends BaseEntity {
	
	@NotBlank
	private String username;
	
	@NotBlank
	private String description;
	
	private Date creationDate;
	
	private Boolean edited;

}
