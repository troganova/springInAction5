package tacos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter.Mode.SAMEORIGIN;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final ReactiveUserDetailsService userDetailsService;

    public SecurityConfig(ReactiveUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
//    @ConditionalOnMissingBean //нужно для работы теста
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(authorize -> authorize
                        .pathMatchers(HttpMethod.PATCH, "/api/ingredients").permitAll()
                        .pathMatchers(HttpMethod.PATCH, "/data-api/ingredients").permitAll()
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()

                        .pathMatchers("/design", "/orders").hasRole("USER")
                        .pathMatchers("/api/**", "/data-api/**").permitAll()
                        .anyExchange().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                )
                .logout(logout -> logout
                        .logoutUrl("/")
                )
                .csrf(csrf -> csrf
                        .disable() //("/h2-console/**", "/api/**", "/data-api/**")
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions
                                .mode(SAMEORIGIN))
                )
                .authenticationManager(reactiveAuthenticationManager())
                .build();
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager manager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        manager.setPasswordEncoder(passwordEncoder());
        return manager;
    }
}