package ISPP.G5.INDVELOPERS.models;

import java.awt.Image;

import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document(collection = "Publication")
@AllArgsConstructor
public class Publication extends BaseEntity {

	@NotBlank
	private String username;

	private Image userPicture;

	@NotBlank
	private String text;

	private Image imagen;

}
