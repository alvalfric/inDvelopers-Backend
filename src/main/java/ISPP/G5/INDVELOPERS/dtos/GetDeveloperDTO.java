package ISPP.G5.INDVELOPERS.dtos;

import java.awt.Image;
import java.util.List;
import java.util.Set;

import ISPP.G5.INDVELOPERS.models.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetDeveloperDTO {

	private String id;

	private String username;

	private String email;

	private List<String> gameList;

	private String userImage;

	private Set<UserRole> roles;

	private String description;

	private String technologies;

	private Boolean isPremium;
}
