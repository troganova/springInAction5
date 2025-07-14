package tacos;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailOrderService {

  private final UserRepository userRepo;
  private final IngredientRepository ingredientRepo;
  private final PaymentMethodRepository paymentMethodRepo;
  
  public TacoOrder convertEmailOrderToDomainOrder(EmailOrder emailOrder) {
      User user = userRepo.findByEmail(emailOrder.getEmail());
      TacoOrder order = new TacoOrder();
      if(user != null) {
          PaymentMethod paymentMethod = paymentMethodRepo.findByUser(user);
          if(paymentMethod != null) {
              order.setCcNumber(paymentMethod.getCcNumber());
              order.setCcCVV(paymentMethod.getCcCVV());
              order.setCcExpiration(paymentMethod.getCcExpiration());
          }
          order.setDeliveryName(user.getFullname());
          order.setDeliveryStreet(user.getStreet());
          order.setDeliveryCity(user.getCity());
          order.setDeliveryState(user.getState());
          order.setDeliveryZip(user.getZip());
      }

      emailOrder.getTacos().forEach(emailTaco -> {
          Taco taco = new Taco();
          taco.setName(emailTaco.getName());

          List<String> ingredientIds = emailTaco.getIngredients();
          ingredientIds.forEach(ingredient -> {
              taco.addIngredient(ingredientRepo.findById(ingredient).get());
          });
          order.addTaco(taco);
      });
      return order;
  }
  
}
