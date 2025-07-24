package tacos;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EmailOrderService {

  private final UserRepository userRepo;
  private final IngredientRepository ingredientRepo;
  private final PaymentMethodRepository paymentMethodRepo;
  
  public Mono<TacoOrder> convertEmailOrderToDomainOrder(Mono<EmailOrder> emailOrder) {
    return emailOrder.flatMap(eOrder -> {
      Mono<User> userMono = userRepo.findByEmail(eOrder.getEmail());
      
      Mono<PaymentMethod> paymentMono = userMono.flatMap(user -> paymentMethodRepo.findByUserId(user.getId()));

      return Mono.zip(userMono, paymentMono)
          .flatMap(tuple -> {
            User user = tuple.getT1();
            PaymentMethod paymentMethod = tuple.getT2();
            TacoOrder order = new TacoOrder();
//            order.setUser(user);
            order.setCcNumber(paymentMethod.getCcNumber());
            order.setCcCVV(paymentMethod.getCcCVV());
            order.setCcExpiration(paymentMethod.getCcExpiration());
            order.setDeliveryName(user.getFullname());
            order.setDeliveryStreet(user.getStreet());
            order.setDeliveryCity(user.getCity());
            order.setDeliveryState(user.getState());
            order.setDeliveryZip(user.getZip());
//            order.setPlacedAt(new Date());
            
            return emailOrder.map(eOrd -> {
              List<EmailOrder.EmailTaco> emailTacos = eOrd.getTacos();
              for (EmailOrder.EmailTaco emailTaco : emailTacos) {
                Taco taco = new Taco();
                taco.setName(emailTaco.getName());

                List<String> ingredientIds = emailTaco.getIngredients();
                for (String ingredientId : ingredientIds) {
                  Mono<Ingredient> ingredientMono = ingredientRepo.findById(ingredientId);
                  ingredientMono.subscribe(ingredient -> 
                      taco.addIngredient(ingredient));
                }
                order.addTaco(taco);
              }
              return order;
            });
          });
    });
  }
  
}
