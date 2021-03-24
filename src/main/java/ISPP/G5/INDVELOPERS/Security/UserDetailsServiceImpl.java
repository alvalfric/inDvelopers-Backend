package ISPP.G5.INDVELOPERS.Security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ISPP.G5.INDVELOPERS.models.Developer;
import ISPP.G5.INDVELOPERS.models.UserEntity;
import ISPP.G5.INDVELOPERS.repositories.DeveloperRepository;
import ISPP.G5.INDVELOPERS.repositories.UserEntityRepository;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final DeveloperRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Developer developer;

        developer = repository.findByUsername(username).orElse(null);

        if(developer != null) {
            return User//
                    .withUsername(username)//
                    .password(developer.getPassword())//
                    .authorities(developer.getAuthorities())//
                    .accountExpired(false)//
                    .accountLocked(false)//
                    .credentialsExpired(false)//
                    .disabled(false)//
                    .build();
        } else {
            throw new UsernameNotFoundException("username not found");
        }
    }
}
