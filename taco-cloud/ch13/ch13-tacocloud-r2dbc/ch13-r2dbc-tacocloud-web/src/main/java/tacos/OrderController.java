package tacos;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
@RequiredArgsConstructor
public class OrderController {

  private final OrderRepository orderRepository;
  private final OrderMessagingService orderMessagingService;

  @GetMapping("/current")
  public String orderForm(@AuthenticationPrincipal User user,
                          @ModelAttribute TacoOrder order) {
    if (order.getDeliveryName() == null) {
      order.setDeliveryName(user.getFullname());
    }
    if (order.getDeliveryStreet() == null) {
      order.setDeliveryStreet(user.getStreet());
    }
    if (order.getDeliveryCity() == null) {
      order.setDeliveryCity(user.getCity());
    }
    if (order.getDeliveryState() == null) {
      order.setDeliveryState(user.getState());
    }
    if (order.getDeliveryZip() == null) {
      order.setDeliveryZip(user.getZip());
    }
    return "orderForm";
  }

  @PostMapping
  public String processOrder(@Valid TacoOrder order, Errors errors,
                             SessionStatus sessionStatus, @AuthenticationPrincipal User user) {
    if (errors.hasErrors()) {
      return "orderForm";
    }

    orderRepository.save(order);
    orderMessagingService.sendOrder(order);
    log.info("Order submitted: {}", order);
    sessionStatus.setComplete();

    return "redirect:/";
  }

  @GetMapping
  public String ordersForUser(
          @AuthenticationPrincipal User user, Model model) {
//    model.addAttribute("orders", orderRepository.findByUserId(user.getId()).take(20));
    return "orderList";
  }
}
