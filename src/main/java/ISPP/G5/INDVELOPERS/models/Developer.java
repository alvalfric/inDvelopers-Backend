package ISPP.G5.INDVELOPERS.models;


import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import ISPP.G5.INDVELOPERS.Utils.CustomAuthorityDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Document(collection="Developer")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Developer extends BaseEntity implements UserDetails{

	private static final long serialVersionUID = 88749523013034397L;

	@NotBlank
	private String username;
	
	@NotBlank
	private String password;
	
	@NotBlank
	@Email
	private String email;
	
	
	private String userImage;
	
	private Set<UserRole> roles;
	
	@NotBlank
	private String description;
	
	@NotBlank
	private String technologies;
	
	private Boolean isPremium;
	
	@NotBlank
	private Date dateOfBirth;
	
	@DBRef(lazy = true)
	private List<Developer> following;

	@Override
	@JsonDeserialize(using = CustomAuthorityDeserializer.class)
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		 return roles.stream().map(ur -> new SimpleGrantedAuthority("ROLE_"+ur.name())).collect(Collectors.toList());
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	public Boolean hasAuthority(String aut) {
		return this.getAuthorities().stream().anyMatch(a -> a.equals(new SimpleGrantedAuthority(aut)));
	}
	
}

