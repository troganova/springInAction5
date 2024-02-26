package tacos.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import tacos.domain.*;
import tacos.data.IngredientRepository;
import tacos.data.PaymentMethodRepository;
import tacos.data.UserRepository;
import tacos.api.EmailOrder.EmailTaco;

@Service
public class EmailOrderService {

  private UserRepository userRepo;
  private IngredientRepository ingredientRepo;
  private PaymentMethodRepository paymentMethodRepo;

  public EmailOrderService(UserRepository userRepo, IngredientRepository ingredientRepo,
      PaymentMethodRepository paymentMethodRepo) {
    this.userRepo = userRepo;
    this.ingredientRepo = ingredientRepo;
    this.paymentMethodRepo = paymentMethodRepo;
  }

  public Order convertEmailOrderToDomainOrder(EmailOrder emailOrder) {
    User user = userRepo.findByEmail(emailOrder.getEmail());
    PaymentMethod paymentMethod = paymentMethodRepo.findByUserId(user.getId());

    Order order = new Order();
    order.setUser(user);
    order.setCcNumber(paymentMethod.getCcNumber());
    order.setCcCVV(paymentMethod.getCcCVV());
    order.setCcExpiration(paymentMethod.getCcExpiration());
    order.setName(user.getFullname());
    order.setStreet(user.getStreet());
    order.setCity(user.getCity());
    order.setState(user.getState());
    order.setZip(user.getZip());
    order.setPlacedAt(new Date());

    List<EmailTaco> emailTacos = emailOrder.getTacos();
    for (EmailTaco emailTaco : emailTacos) {
      Taco taco = new Taco();
      taco.setName(emailTaco.getName());
      List<String> ingredientIds = emailTaco.getIngredients();
      List<Ingredient> ingredients = new ArrayList<>();
      for (String ingredientId : ingredientIds) {
        Optional<Ingredient> optionalIngredient = ingredientRepo.findById(ingredientId);
        if (optionalIngredient.isPresent()) {
          ingredients.add(optionalIngredient.get());
        }
      }
      taco.setIngredients(ingredients);
      order.addDesign(taco);
    }

    return order;
  }

}
