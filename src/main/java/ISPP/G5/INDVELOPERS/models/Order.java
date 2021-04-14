package ISPP.G5.INDVELOPERS.models;

import org.springframework.data.mongodb.core.mapping.Document;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Document(collection = "Order")
@AllArgsConstructor
@ToString
public class Order extends BaseEntity {
	private Double price;
	private String currency;
	private String method;
	private String intent;
	private String description;
	private String payeeEmail;
}
