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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
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
  public String orderForm() {
    return "orderForm";
  }

  @PostMapping
  public String processOrder(@Valid TacoOrder order, Errors errors,
                             SessionStatus sessionStatus, @AuthenticationPrincipal User user) {
    if (errors.hasErrors()) {
      return "orderForm";
    }
    order.setUser(user);
    orderRepository.save(order);
    orderMessagingService.sendOrder(order);
    log.info("Order submitted: {}", order);
    sessionStatus.setComplete();

    return "redirect:/";
  }

  @GetMapping
  public String ordersForUser(
          @AuthenticationPrincipal User user, Model model) {
    Pageable pageable = PageRequest.of(0, 20);
    model.addAttribute("orders",
            orderRepository.findByUserOrderByPlacedAtDesc(user, pageable));
    return "orderList";
  }
}
