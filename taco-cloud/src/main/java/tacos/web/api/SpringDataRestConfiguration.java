package tacos.web.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import tacos.domain.Taco;


@Configuration
public class SpringDataRestConfiguration {

  @Bean
  public RepresentationModelProcessor<PagedModel<Taco>>
    tacoProcessor(EntityLinks links) {

    return new RepresentationModelProcessor<PagedModel<Taco>>() {
      @Override
      public PagedModel<Taco> process(PagedModel<Taco> resource) {
        resource.add(
            links.linkFor(Taco.class)
                 .slash("recent")
                 .withRel("recents"));
        return resource;
      }
    };
  }
  
}
