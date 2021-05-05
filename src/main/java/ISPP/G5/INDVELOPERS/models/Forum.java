package ISPP.G5.INDVELOPERS.models;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document(collection = "Forum")
@AllArgsConstructor
public class Forum extends BaseEntity {

	@NotBlank
	private String username;

	@NotBlank
	private String title;

	private Date creationDate;

	@DBRef
	private List<Commentary> comments;

}
