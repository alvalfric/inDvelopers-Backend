package ISPP.G5.INDVELOPERS.models;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document(collection="AdminDashboard")
@AllArgsConstructor
public class AdminDashboard extends BaseEntity {
	
    private Integer numGamesDone;
	
	private Integer numReviews;
	
	private Integer numPublications;
	
	private Integer numGamesOwned;

}
