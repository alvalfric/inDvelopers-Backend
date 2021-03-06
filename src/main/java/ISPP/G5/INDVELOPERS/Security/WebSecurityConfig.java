package ISPP.G5.INDVELOPERS.Security;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Value("${security.cors.domains}")
  private List<String> corsDomains;

  @Value("${security.cors.methods}")
  private List<String> corsMethods;

  @Value("${security.cors.headers}")
  private List<String> corsHeaders;

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    // Disable CSRF (cross site request forgery)
    http.csrf().disable();//
    http.cors();

    // No session will be created or used by spring security
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    // Entry points
    http.authorizeRequests()
            //.antMatchers("/**").permitAll()
            .antMatchers("/developers/sign-up").permitAll()
            .antMatchers("/developers/login").permitAll()
            .antMatchers("/developers/recoverPasswordByEmail").permitAll()
            .antMatchers("/developers/restorePassword/*").permitAll()
            .antMatchers("/games/findVerified").permitAll()
            .antMatchers("/games/*").permitAll()
            .antMatchers("/games/findByPrice/**").permitAll()
            .antMatchers("/games/findByTitleVerifiedOrCategorie/**").permitAll()
            .antMatchers("/spam/**").permitAll()
            .antMatchers("/reviews/game/*").permitAll()
            .antMatchers("/publications/**").permitAll()
            .antMatchers("/forums/findAll").permitAll()
            .antMatchers("/comments/findByForum/*").permitAll()
            .antMatchers("/categories/**").permitAll()
            .anyRequest().authenticated();

    // If a user try to access a resource without having enough permissions
    http.exceptionHandling().accessDeniedPage("/login");

    // Apply JWT
    http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));

    // Optional, if you want to test the API from a browser
    http.httpBasic();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(this.corsDomains);
    configuration.setAllowedMethods(this.corsMethods);
    configuration.setAllowedHeaders(this.corsHeaders);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

}
