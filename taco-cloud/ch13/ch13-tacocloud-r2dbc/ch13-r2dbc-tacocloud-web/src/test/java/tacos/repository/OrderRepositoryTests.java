package tacos.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import tacos.*;
import tacos.Ingredient.Type;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataR2dbcTest
@DirtiesContext
public class OrderRepositoryTests {

    @Autowired
    OrderRepository orderRepo;
    @Autowired
    TacoRepository tacoRepo;
    @Autowired
    IngredientRepository ingredientRepo;

    @Test
    public void saveOrderWithTwoTacos() {
        // 1. Сохраняем ингредиенты и получаем их реальные ID
        List<Ingredient> savedIngredients = Flux.just(
                        new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
                        new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
                        new Ingredient("CHED", "Shredded Cheddar", Type.CHEESE),
                        new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
                        new Ingredient("CARN", "Carnitas", Type.PROTEIN),
                        new Ingredient("JACK", "Monterrey Jack", Type.CHEESE)
                )
                .flatMap(ingredientRepo::save)
                .collectList()
                .block();

        // 2. Получаем ID сохраненных ингредиентов
        Long[] ingredientIds = savedIngredients.stream()
                .map(Ingredient::getId)
                .toArray(Long[]::new);

        // 3. Создаем тако с реальными ID ингредиентов
        Mono<Taco> taco1Mono = tacoRepo.save(
                new Taco("Taco One", Set.of(ingredientIds[0], ingredientIds[1], ingredientIds[2])));

        Mono<Taco> taco2Mono = tacoRepo.save(
                new Taco("Taco Two", Set.of(ingredientIds[3], ingredientIds[4], ingredientIds[5])));

        // 4. Создаем и проверяем заказ
        Mono.zip(taco1Mono, taco2Mono)
                .flatMap(tuple -> {
                    TacoOrder order = new TacoOrder();
                    order.setDeliveryName("Test McTest");
                    order.setDeliveryStreet("1234 Test Lane");
                    order.setDeliveryCity("Testville");
                    order.setDeliveryState("CO");
                    order.setDeliveryZip("80123");
                    order.setCcNumber("4111111111111111");
                    order.setCcExpiration("10/23");
                    order.setCcCVV("123");
                    order.addTaco(tuple.getT1());
                    order.addTaco(tuple.getT2());
                    return orderRepo.save(order);
                })
                .as(StepVerifier::create)
                .assertNext(savedOrder -> {
                    assertThat(savedOrder.getId()).isNotNull();
                    // Дополнительные проверки
                })
                .verifyComplete();
    }
}
