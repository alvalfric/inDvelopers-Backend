package ISPP.G5.INDVELOPERS.models;

import org.springframework.data.annotation.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class BaseEntity {

	@Id
	private String id;
	
	
	/*
	 * Entidad base de la que heredan todas las clases que se persisten
	 * En MongoDB, el ID es string y no int. 
	 */
}
