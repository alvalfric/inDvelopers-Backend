package ISPP.G5.INDVELOPERS.models;

import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document(collection="Game")
@AllArgsConstructor
public class Game extends BaseEntity{

	@NotBlank
	@Indexed(unique=true)
	private String title;
	
	@NotBlank
	private String description;
	
	@NotBlank
	private String requirements;
	
	private Double price;
	
	
	
	private Boolean isNotMalware;
	
	
	
	
}
