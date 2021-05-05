package ISPP.G5.INDVELOPERS.models;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class LoginData {
	
	@NotBlank
	private String username;
	@NotBlank
	private String password;
	
	

}
