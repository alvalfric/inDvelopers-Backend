package ISPP.G5.INDVELOPERS.models;

import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document(collection="Categoria")
@AllArgsConstructor
public class Categoria extends BaseEntity {

	@NotBlank
	private String title;
}
