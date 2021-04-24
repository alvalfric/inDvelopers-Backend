package ISPP.G5.INDVELOPERS.models;

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
	
	private Integer numGamesCreated;
	
	private Integer numReviewsCreated;
	
	private Integer numPublicationsCreated;
	
	private Integer numGamesOwned;
	
	private Double moneyEarned;
	
	private Integer totalFollowers;
	
	private Integer gamesNotVerified;
	
	private Integer gamesVerified;
	
	private Integer totalIncident;
	
	private Integer incidentsNotSolved;
	
	private Integer incidentsSolved;

	private Double reviewsMean;

}
