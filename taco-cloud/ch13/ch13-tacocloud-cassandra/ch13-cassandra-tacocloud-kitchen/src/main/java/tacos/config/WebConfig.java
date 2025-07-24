package tacos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.permanentRedirect;

import static java.net.URI.create;

@Profile({"jms-template", "rabbitmq-template"})
@Configuration
public class WebConfig implements WebFluxConfigurer {

  @Bean
  public RouterFunction<ServerResponse> redirectRouter() {
    return route(GET("/"),
            req -> permanentRedirect(create("/orders/receive")).build());
  }
  
}
