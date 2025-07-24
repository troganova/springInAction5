package tacos;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path="/api/orders", produces="application/json")
@CrossOrigin(origins="http://tacocloud:8080")
@RequiredArgsConstructor
public class OrderApiController {

  private final OrderRepository repo;
  private final OrderMessagingService messagingService;
  private final TacoOrderAggregateService tacoOrderAggregateService;


  @GetMapping(produces="application/json")
  public Flux<TacoOrder> allOrders() {
    return repo.findAll();
  }

  @PostMapping(consumes="application/json")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<TacoOrder> postOrder(@RequestBody TacoOrder order) {
    messagingService.sendOrder(order);
    return tacoOrderAggregateService.save(order);
  }

  @PutMapping(path="/{orderId}", consumes="application/json")
  public Mono<TacoOrder> putOrder(@RequestBody Mono<TacoOrder> order) {
    return order.flatMap(repo::save);
  }

  @PatchMapping(path="/{orderId}", consumes="application/json")
  public Mono<TacoOrder> patchOrder(@PathVariable("orderId") Long orderId,
                          @RequestBody TacoOrder patch) {

    return repo.findById(orderId)
            .map(order -> {
                if (patch.getDeliveryName() != null) {
                  order.setDeliveryName(patch.getDeliveryName());
                }
                if (patch.getDeliveryStreet() != null) {
                  order.setDeliveryStreet(patch.getDeliveryStreet());
                }
                if (patch.getDeliveryCity() != null) {
                  order.setDeliveryCity(patch.getDeliveryCity());
                }
                if (patch.getDeliveryState() != null) {
                  order.setDeliveryState(patch.getDeliveryState());
                }
                if (patch.getDeliveryZip() != null) {
                  order.setDeliveryZip(patch.getDeliveryZip());
                }
                if (patch.getCcNumber() != null) {
                  order.setCcNumber(patch.getCcNumber());
                }
                if (patch.getCcExpiration() != null) {
                  order.setCcExpiration(patch.getCcExpiration());
                }
                if (patch.getCcCVV() != null) {
                  order.setCcCVV(patch.getCcCVV());
                }
                return order;
            })
            .flatMap(repo::save);
  }

  @DeleteMapping("/{orderId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteOrder(@PathVariable("orderId") Long orderId) {
    try {
      repo.deleteById(orderId);
    } catch (EmptyResultDataAccessException e) {}
  }

}
