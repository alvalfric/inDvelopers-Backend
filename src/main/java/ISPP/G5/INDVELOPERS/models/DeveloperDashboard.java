package ISPP.G5.INDVELOPERS.models;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document(collection="DeveloperDashboard")
@AllArgsConstructor
public class DeveloperDashboard extends BaseEntity {
	
	private Float numGamesDone;
	
	private Float numReviews;
	
	private Float numPublications;
	
	private Float numGamesOwned;
	
	

}