
package ISPP.G5.INDVELOPERS.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
	private double	score;
}
