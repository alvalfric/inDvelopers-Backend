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
public class AdminDashboard {
	
    private Integer totalGamesCreated;
	
	private Integer totalReviewsCreated;
	
	private Integer totalPublicationsCreated;
	
	private Integer totalGamesPurchased;
	
	private Double totalMoneyEarnedByDevelopers;
	
	private Integer totalDevelopers;
		
	private Integer gamesNotVerified;
	
	private Integer gamesVerified;
	
	private Integer totalIncident;
	
	private Integer incidentsNotSolved;
	
	private Integer incidentsSolved;
	
	private Integer totalPremiumUsers;
	
	private Integer totalNotPremiumUsers;
	
	
	

}
