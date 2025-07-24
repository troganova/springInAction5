package tacos;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping(path="/api/tacos", produces="application/json")
@CrossOrigin(origins="http://tacocloud:8080")
@RequiredArgsConstructor
public class TacoController {

  private final TacoRepository tacoRepository;

  @GetMapping(params="recent")
  public Flux<Taco> recentTacos() {
    return tacoRepository
            .findAll()
            .take(12);
  }

  @PostMapping(consumes = "application/json")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Taco> postTaco(@RequestBody Mono<Taco> taco) {
    return taco.flatMap(tacoRepository::save);
  }

  @GetMapping("/{id}")
  public Mono<Taco> tacoById(@PathVariable("id") UUID id) {
    return tacoRepository.findById(id);
  }
}
