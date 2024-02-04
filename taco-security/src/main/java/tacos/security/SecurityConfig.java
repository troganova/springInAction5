package tacos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.ldap.EmbeddedLdapServerContextSourceFactoryBean;
import org.springframework.security.config.ldap.LdapPasswordComparisonAuthenticationManagerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) -> authz
                        .requestMatchers(new AntPathRequestMatcher("/design"), new AntPathRequestMatcher("/orders")).hasRole("USER")
                        .anyRequest().permitAll()
                )
                .headers(headerss -> headerss.frameOptions(frameOptionsConfig -> frameOptionsConfig.sameOrigin()))
                .formLogin(form ->
                            form
                                .loginPage("/login")
                                .defaultSuccessUrl("/design", true))

                .logout(form -> form.logoutSuccessUrl("/"))
//                .csrf(csrff -> csrff.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")));
                .csrf(csrff -> csrff.disable());

        return http.build();
    }

    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Profile("jdbc")
    public DaoAuthenticationProvider authenticationProvider(PasswordEncoder encoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder);
        return authProvider;
    }

    //jdbc-based authentication
    @Autowired
    DataSource dataSource;

    @Bean
    @Profile("jdbcDC")
    public UserDetailsManager users(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery("select username, password, enabled from Users where username=?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select username, authority from UserAuthorities where username=?");
        return jdbcUserDetailsManager;
    }

    //in memory
    @Bean
    @Profile("inmemory")
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.builder()
                .username("buzz")
                .password("infinity")
                .roles("USER")
                .build();
        UserDetails user2 = User.builder()
                .username("wood")
                .password("bullseye")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user, user2);
    }

    // ldap
    @Bean
    @Profile("ldap")
    AuthenticationManager ldapAuthenticationManager(
            BaseLdapPathContextSource contextSource,
            LdapAuthoritiesPopulator authorities) {
        LdapPasswordComparisonAuthenticationManagerFactory factory = new LdapPasswordComparisonAuthenticationManagerFactory(
                contextSource, NoOpPasswordEncoder.getInstance());
        factory.setUserSearchBase("ou=people");
        factory.setUserSearchFilter("(uid={0})");
        factory.setLdapAuthoritiesPopulator(authorities);
        return factory.createAuthenticationManager();
    }

    @Bean
    @Profile("ldap")
    LdapAuthoritiesPopulator authorities(BaseLdapPathContextSource contextSource) {
        String groupSearchBase = "ou=groups";
        DefaultLdapAuthoritiesPopulator authorities = new DefaultLdapAuthoritiesPopulator
                (contextSource, groupSearchBase);
        authorities.setGroupSearchFilter("(member={0})");
        return authorities;
    }

    //embedded ldap server
    @Bean
    @Profile("ldap")
    public EmbeddedLdapServerContextSourceFactoryBean contextSourceFactoryBean() {
        EmbeddedLdapServerContextSourceFactoryBean contextSourceFactoryBean =
                EmbeddedLdapServerContextSourceFactoryBean.fromEmbeddedLdapServer();
        contextSourceFactoryBean.setRoot("dc=tacocloud,dc=com");
        contextSourceFactoryBean.setLdif("classpath:users.ldif");
        contextSourceFactoryBean.setPort(8389);
        return contextSourceFactoryBean;
    }
}
