package tacos;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class WebConfig implements WebFluxConfigurer {

  @Bean
  public RouterFunction<ServerResponse> routerFunction() {
    return route(GET("/"), request ->
            ServerResponse.ok().render("home"))
            .andRoute(GET("/login"), request ->
                    ServerResponse.ok().render("login"));
  }

}
