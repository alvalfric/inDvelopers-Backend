
package ISPP.G5.INDVELOPERS.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document(collection = "Review")
@AllArgsConstructor
public class Review extends BaseEntity {

	@NotBlank
	private String	text;
	
	@NotNull
	@Positive
	private double	score;
	
	@NotNull
	@DBRef
	private Game	game;
	
	@NotNull
	@DBRef
	private Developer	developer;
}
