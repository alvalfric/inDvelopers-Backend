package ISPP.G5.INDVELOPERS.models;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document(collection="OwnedGame")
@AllArgsConstructor
@Builder
public class OwnedGame extends BaseEntity{
	
	@DBRef
	private Developer buyer;
	
	@DBRef
	private List<Game> ownedGames;
}
