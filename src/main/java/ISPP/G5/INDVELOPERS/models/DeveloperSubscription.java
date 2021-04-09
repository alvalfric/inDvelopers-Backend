package ISPP.G5.INDVELOPERS.models;


import java.time.LocalDate;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document(collection="DeveloperSubscription")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeveloperSubscription extends BaseEntity{

	@NotBlank
	@UniqueElements
	Developer developer;
	
	@NotBlank
	LocalDate startDate;
	
	@NotBlank
	LocalDate endDate;
}
