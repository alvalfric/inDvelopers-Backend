package ISPP.G5.INDVELOPERS.models;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document(collection="Category")
@AllArgsConstructor
@NoArgsConstructor
public class Category extends BaseEntity {

	@NotBlank
	private String title;

}
