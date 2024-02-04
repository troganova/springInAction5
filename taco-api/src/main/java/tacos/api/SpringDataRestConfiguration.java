package tacos.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import tacos.domain.Ingredient;
import tacos.domain.Order;
import tacos.domain.Taco;
import tacos.domain.User;


@Configuration
public class SpringDataRestConfiguration {

  @Bean
  public RepresentationModelProcessor<PagedModel<EntityModel<Taco>>> tacoProcessor(EntityLinks links) {
    return new RepresentationModelProcessor<PagedModel<EntityModel<Taco>>>() {
      @Override
      public PagedModel<EntityModel<Taco>> process(PagedModel<EntityModel<Taco>> resource) {
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
