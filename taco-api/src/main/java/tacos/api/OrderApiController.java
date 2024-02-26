package tacos.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import tacos.domain.Order;
import tacos.data.OrderRepository;
import tacos.messaging.OrderMessagingService;

@RestController
@RequestMapping(path="/orders", produces="application/json")
@CrossOrigin(origins="*")
public class OrderApiController {

  private OrderRepository repo;
  private OrderMessagingService orderMessages;
  private EmailOrderService emailOrderService;

  @Autowired
  public OrderApiController(OrderRepository repo, 
                            OrderMessagingService orderMessages,
                            EmailOrderService emailOrderService) {
    this.repo = repo;
    this.orderMessages = orderMessages;
    this.emailOrderService = emailOrderService;
  }
  
  @GetMapping(produces="application/json")
  public Iterable<Order> allOrders() {
    return repo.findAll();
  }
  
  @PostMapping(consumes="application/json")
  @ResponseStatus(HttpStatus.CREATED)
  public Order postOrder(@RequestBody Order order) {
    orderMessages.sendOrder(order);
    return repo.save(order);
  }

  @PostMapping(path="fromEmail", consumes="application/json")
  @ResponseStatus(HttpStatus.CREATED)
  public Order postOrderFromEmail(@RequestBody EmailOrder emailOrder) {
    Order order = emailOrderService.convertEmailOrderToDomainOrder(emailOrder);
    orderMessages.sendOrder(order);
    return repo.save(order);
  }

  @PutMapping(path="/{orderId}", consumes="application/json")
  public Order putOrder(@RequestBody Order order) {
    return repo.save(order);
  }

  @PatchMapping(path="/{orderId}", consumes="application/json")
  public Order patchOrder(@PathVariable("orderId") Long orderId,
                          @RequestBody Order patch) {
    
    Order order = repo.findById(orderId).get();
    if (patch.getName() != null) {
      order.setName(patch.getName());
    }
    if (patch.getStreet() != null) {
      order.setStreet(patch.getStreet());
    }
    if (patch.getCity() != null) {
      order.setCity(patch.getCity());
    }
    if (patch.getState() != null) {
      order.setState(patch.getState());
    }
    if (patch.getZip() != null) {
      order.setZip(patch.getState());
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
    return repo.save(order);
  }
  
  @DeleteMapping("/{orderId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteOrder(@PathVariable("orderId") Long orderId) {
    try {
      repo.deleteById(orderId);
    } catch (EmptyResultDataAccessException e) {}
  }

}
