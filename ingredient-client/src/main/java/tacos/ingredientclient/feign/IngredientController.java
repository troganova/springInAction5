package tacos.ingredientclient.feign;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.spring6.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;

@Controller
@RequestMapping("/ingredients")
@Profile("feign")
@Slf4j
public class IngredientController {

  private IngredientClient client;

  public IngredientController(IngredientClient client) {
    this.client = client;
  }
  
  @GetMapping
  public String ingredientList(Model model) {
    log.info("Fetched all ingredients from a Feign client.");
    IReactiveDataDriverContextVariable variable =
            new ReactiveDataDriverContextVariable(client.getAllIngredients(), 1);
    model.addAttribute("ingredients", variable);
    return "ingredientList";
  }
  
  @GetMapping("/{id}")
  public String ingredientDetailPage(@PathVariable("id") String id, Model model) {
    log.info("Fetched an ingredient from a Feign client.");
    IReactiveDataDriverContextVariable variable =
            new ReactiveDataDriverContextVariable(client.getIngredient(id).flux(), 1);
    model.addAttribute("ingredients", variable);
    return "ingredientDetail";
  }
  
}
