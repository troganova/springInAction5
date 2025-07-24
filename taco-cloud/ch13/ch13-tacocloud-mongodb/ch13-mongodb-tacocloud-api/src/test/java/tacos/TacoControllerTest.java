package tacos;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = TacoController.class)
public class TacoControllerTest {

  @Test
  public void shouldReturnRecentTacos() {
    // Создаем 12 тестовых тако (как в ваших проверках)
    List<Taco> tacos = Flux.range(1, 12)
            .map(i -> testTaco(i.longValue()))
            .collectList()
            .block();

    Flux<Taco> tacoFlux = Flux.fromIterable(tacos);
    TacoRepository tacoRepo = Mockito.mock(TacoRepository.class);
    IngredientRepository ingredientRepo = Mockito.mock(IngredientRepository.class);

    when(tacoRepo.findAll()).thenReturn(tacoFlux);
    when(ingredientRepo.findById("INGA")).thenReturn(Mono.just(new Ingredient("INGA", "Ingredient A", Ingredient.Type.WRAP)));
    when(ingredientRepo.findById("INGB")).thenReturn(Mono.just(new Ingredient("INGB", "Ingredient B", Ingredient.Type.PROTEIN)));

    WebTestClient testClient = WebTestClient.bindToController(new TacoController(tacoRepo)).build();
    testClient.get().uri("/api/tacos?recent")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$").isArray()
            .jsonPath("$").isNotEmpty()
            .jsonPath("$[0].id").isEqualTo(tacos.get(0).getId().toString())
            .jsonPath("$[0].name").isEqualTo("Taco 1")
            .jsonPath("$[1].id").isEqualTo(tacos.get(1).getId().toString())
            .jsonPath("$[1].name").isEqualTo("Taco 2")
            .jsonPath("$[11].id").isEqualTo(tacos.get(11).getId().toString())
            .jsonPath("$[11].name").isEqualTo("Taco 12")
            .jsonPath("$[12]").doesNotExist();
  }

  @Test
  public void shouldSaveATaco() {
    TacoRepository tacoRepo = Mockito.mock(TacoRepository.class);

    WebTestClient testClient = WebTestClient.bindToController(new TacoController(tacoRepo)).build();
    Mono<Taco> unsavedTacoMono = Mono.just(testTaco(1L));
    Taco savedTaco = testTaco(1L);
    Mono<Taco> savedTacoMono = Mono.just(savedTaco);

    when(tacoRepo.save(any())).thenReturn(savedTacoMono);

    testClient.post()
            .uri("/api/tacos")
            .contentType(MediaType.APPLICATION_JSON)
            .body(unsavedTacoMono, Taco.class)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(Taco.class)
            .isEqualTo(savedTaco);
  }

  private Taco testTaco(Long id) {
    Taco taco = new Taco();
    taco.setId(id.toString());
    taco.setName("Taco " + id);
    taco.addIngredient(new Ingredient("INGA", "Ingredient A", Ingredient.Type.WRAP));
    taco.addIngredient(new Ingredient("INGB", "Ingredient B", Ingredient.Type.PROTEIN));
    return taco;
  }
}