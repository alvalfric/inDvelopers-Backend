package ISPP.G5.INDVELOPERS.models;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document(collection = "Incident")
@AllArgsConstructor
public class Incident extends BaseEntity {

	@NotBlank
	private String	title;
	
	@NotBlank
	private String	description;

	private String cause;
	
	@NotBlank
	private LocalDate date;
	
	private String imagen;
	
	private boolean solved;
	
	@NotNull
	@DBRef
	private Developer	developer;
}
