package tacos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path="/api/ingredients", produces="application/json")
@CrossOrigin(origins="http://localhost:8080")
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

  @GetMapping("/slug/{id}")
  public Mono<Ingredient> getIngredientBySlug(@PathVariable String id) {
    return repo.findBySlug(id);
  }

  @PutMapping
  public Mono<Ingredient> putIngredient(@RequestBody Ingredient ingredient) {
    return repo.existsBySlug(ingredient.getSlug()).flatMap(exists -> exists ? repo.save(ingredient) : Mono.empty());
  }

  @PostMapping
  public Mono<Ingredient> saveIngredient(@RequestBody Ingredient ingredient) {
    return repo.save(ingredient);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteIngredient(@PathVariable("id") Long ingredientId) {
    repo.findById(ingredientId)
            .doOnNext(repo::delete)
            .subscribe();
  }

}
