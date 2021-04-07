package ISPP.G5.INDVELOPERS.models;

import java.awt.Image;

import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document(collection = "Photo")
@AllArgsConstructor
public class Photo extends BaseEntity{
	
	public Photo() {
		super();
	}

	private String title;
    
    private Binary image;

}
