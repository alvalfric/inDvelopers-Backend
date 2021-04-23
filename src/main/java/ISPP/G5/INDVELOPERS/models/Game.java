package ISPP.G5.INDVELOPERS.models;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document(collection="Game")
@AllArgsConstructor
@NoArgsConstructor
public class Game extends BaseEntity{

	@NotBlank
	private String title;
	
	@NotBlank
	private String description;
	
	@NotBlank
	private String requirements;
	
	@NotNull
	private Double price;
	
	private String idCloud;
	
	private Boolean isNotMalware;
	
	private Developer creator;

	private String imagen;
	
	@DBRef
	private List<Category> categorias;
	
	private Date fechaCreacion;
	
	private Integer pegi;
	
}
