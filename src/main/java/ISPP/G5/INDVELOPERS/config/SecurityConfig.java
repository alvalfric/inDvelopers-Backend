package ISPP.G5.INDVELOPERS.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll().and().cors().and().csrf().disable();
    }
    
    /*
     * Esto desactiva la seguridad en Spring. Solo lo he puesto para comprobar 
     * la comunicación con React y Spring
     */
}