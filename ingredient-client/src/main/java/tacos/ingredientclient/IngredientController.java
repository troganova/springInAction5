package tacos.ingredientclient;

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
@Slf4j
public class IngredientController {

  private IngredientServiceClient client;

  public IngredientController(IngredientServiceClient client) {
    this.client = client;
  }
  
  @GetMapping
  public String ingredientList(Model model) {
    log.info("Fetched all ingredients from a WebClient-based service.");
    IReactiveDataDriverContextVariable variable =
            new ReactiveDataDriverContextVariable(client.getAllIngredients(), 1);
    model.addAttribute("ingredients", variable);
    return "ingredientList";
  }
  
  @GetMapping("/{id}")
  public String ingredientDetailPage(@PathVariable("id") String id, Model model) {
    log.info("Fetched an ingredient from a WebClient-based service.");
    IReactiveDataDriverContextVariable variable =
            new ReactiveDataDriverContextVariable(client.getIngredientById(id).flux(), 1);
    model.addAttribute("ingredients", variable);
    return "ingredientDetail";
  }
  
}
