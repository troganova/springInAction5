package tacos.web.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import tacos.domain.Ingredient;
import tacos.domain.Order;
import tacos.domain.Taco;
import tacos.domain.User;


@Configuration
public class SpringDataRestConfiguration {

  @Bean
  public RepresentationModelProcessor<CollectionModel<Taco>> tacoProcessor(EntityLinks links) {
    return new RepresentationModelProcessor<CollectionModel<Taco>>() {
      @Override
      public CollectionModel<Taco> process(CollectionModel<Taco> resource) {
        resource.add(
            links.linkFor(Taco.class)
                 .slash("recent")
                 .withRel("recents"));
        return resource;
      }
    };
  }

  @Bean
  public RepositoryRestConfigurer repositoryRestConfigurer() {
    return RepositoryRestConfigurer.withConfig(config -> {
      config.exposeIdsFor(Ingredient.class);
      config.exposeIdsFor(Taco.class);
      config.exposeIdsFor(Order.class);
      config.exposeIdsFor(User.class);
    });
  }
  
}
