package tacos;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean //нужно для работы теста
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.PATCH, "/api/ingredients").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/data-api/ingredients").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()

                        .requestMatchers("/design", "/orders").hasRole("USER")
                        .requestMatchers("/api/**", "/data-api/**").permitAll()
                        .requestMatchers("/", "/**").permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                )
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**", "/api/**", "/data-api/**")
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame
                                .sameOrigin()
                        )
                )
                .build();
    }
}