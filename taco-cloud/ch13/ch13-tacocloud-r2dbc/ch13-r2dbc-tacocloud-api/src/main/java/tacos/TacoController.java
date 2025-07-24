package tacos;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path="/api/tacos", produces="application/json")
@CrossOrigin(origins="http://tacocloud:8080")
@RequiredArgsConstructor
public class TacoController {

  private final TacoRepository tacoRepository;
  private final IngredientRepository ingredientRepository;

  @GetMapping(params="recent")
  public Flux<TacoView> recentTacos() {
    return tacoRepository
            .findAll()
            .take(12)
            .map(taco -> {
              TacoView tacoView = new TacoView(taco.getId(), taco.getName());
              taco.getIngredientIds().forEach(ingredientId -> {
                        ingredientRepository.findById(ingredientId)
                                .subscribe(ingredient -> {
                                  tacoView.addIngredient(ingredient);
                                });
                      });
              return tacoView;
            });
  }

  @PostMapping
  public Mono<Taco> postTaco(@RequestBody TacoView tacoView) {
    Taco taco = new Taco(tacoView.getName());
    for (Ingredient ingredient : tacoView.getIngredients()) {
      taco.addIngredient(ingredient);
    }
    return tacoRepository.save(taco);
  }

  @GetMapping("/{id}")
  public Mono<Taco> tacoById(@PathVariable("id") Long id) {
    return tacoRepository.findById(id);
  }
}
