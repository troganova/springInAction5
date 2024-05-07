package tacos.ingredients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path="/ingredients", produces="application/json")
@CrossOrigin(origins="*")
public class IngredientController {

  private IngredientRepository repo;

  @Autowired
  public IngredientController(IngredientRepository repo) {
    this.repo = repo;
  }

  @GetMapping
  public Flux<Ingredient> allIngredients() {
    return repo.findAll();
  }
  
  @GetMapping("/{id}")
  public Mono<Ingredient> byId(@PathVariable String id) {
    return repo.findById(id);
  }
  
  @PutMapping("/{id}")
  public void updateIngredient(@PathVariable String id, @RequestBody Ingredient ingredient) {
    if (!ingredient.getId().equals(id)) {
      throw new IllegalStateException("Given ingredient's ID doesn't match the ID in the path.");
    }
    repo.save(ingredient);
  }

  @PostMapping
  public Mono<Ingredient> postIngredient(@RequestBody Ingredient ingredient) {
    return repo.save(ingredient);
  }
  
  @DeleteMapping("/{id}")
  public void deleteIngredient(@PathVariable String id) {
    repo.deleteById(id);
  }
  
}
