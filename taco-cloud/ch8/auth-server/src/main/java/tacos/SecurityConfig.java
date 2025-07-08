package tacos;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import tacos.users.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	@Order(2)
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
			throws Exception {
		return http
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(
								"/.well-known/**",
								"/oauth2/**"
						).permitAll()
						.anyRequest().authenticated())
				.formLogin(Customizer.withDefaults())
				.csrf(csrf -> csrf.disable())
				.cors(cors -> cors.disable())
				.build();
	}

	@Bean
	public UserDetailsService userDetailsService(UserRepository userRepo) {
	  return username -> userRepo.findByUsername(username);
	}


}
