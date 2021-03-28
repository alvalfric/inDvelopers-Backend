package ISPP.G5.INDVELOPERS.models;

import java.util.Collection;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document(collection="OwnedGame")
@AllArgsConstructor
public class OwnedGame extends BaseEntity{
	
	private Developer buyer;
	
	private Collection<Game> ownedGame;
}
