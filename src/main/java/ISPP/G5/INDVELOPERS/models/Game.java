package ISPP.G5.INDVELOPERS.models;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

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
	private Set<Category> categorias;
	
	private Date fechaCreacion;
	
	private Integer pegi;
	
	@Positive
	@Max(1) // Factor percent (From 0 to 1) Ej: 0.3 equals 30%
	private Double discount;
	
}
