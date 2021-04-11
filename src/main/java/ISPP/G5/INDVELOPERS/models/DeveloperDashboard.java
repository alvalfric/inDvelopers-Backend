package ISPP.G5.INDVELOPERS.models;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DeveloperDashboard {
	
	private Integer numGamesDone;
	
	private Integer numReviews;
	
	private Integer numPublications;
	
	private Integer numGamesOwned;
	
	

}
