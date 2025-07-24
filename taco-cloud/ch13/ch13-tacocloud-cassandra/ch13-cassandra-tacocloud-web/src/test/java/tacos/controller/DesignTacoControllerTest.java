package tacos.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tacos.*;
import tacos.Ingredient.Type;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebFluxTest(DesignTacoController.class)
@Import(SecurityConfig.class)
@Disabled
public class DesignTacoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  private List<Ingredient> ingredients;

  private Taco design;

  @TestConfiguration
  static class TestConfig {
    @Bean
    @Primary
    public OrderRepository orderRepository() {
      return Mockito.mock(OrderRepository.class);
    }

    @Bean
    @Primary
    public IngredientRepository ingredientRepository() {
      return Mockito.mock(IngredientRepository.class);
    }

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
      return Mockito.mock(UserDetailsService.class);
    }
  }

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private IngredientRepository ingredientRepository;

  @Autowired
  private ReactiveUserDetailsService userDetailsService;

  @BeforeEach
  public void setup() {
    ingredients = Arrays.asList(
      new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
      new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
      new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
      new Ingredient("CARN", "Carnitas", Type.PROTEIN),
      new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
      new Ingredient("LETC", "Lettuce", Type.VEGGIES),
      new Ingredient("CHED", "Cheddar", Type.CHEESE),
      new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
      new Ingredient("SLSA", "Salsa", Type.SAUCE),
      new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
    );

    when(ingredientRepository.findAll())
            .thenReturn(Flux.fromIterable(ingredients));

    when(ingredientRepository.findById("FLTO")).thenReturn(Mono.just(new Ingredient("FLTO", "Flour Tortilla", Type.WRAP)));
    when(ingredientRepository.findById("GRBF")).thenReturn(Mono.just(new Ingredient("GRBF", "Ground Beef", Type.PROTEIN)));
    when(ingredientRepository.findById("CHED")).thenReturn(Mono.just(new Ingredient("CHED", "Cheddar", Type.CHEESE)));
    design = new Taco();
    design.setName("Test Taco");

    design.setIngredients(
            Arrays.asList(
                    new IngredientUDT("Flour Tortilla", Type.WRAP),
                    new IngredientUDT( "Ground Beef", Type.PROTEIN),
                    new IngredientUDT("Cheddar", Type.CHEESE)));
  }

  @Test
  @WithMockUser(username="testuser", password="testpass", authorities="ROLE_USER")
  public void testShowDesignForm() throws Exception {
    mockMvc.perform(get("/design"))
        .andExpect(status().isOk())
        .andExpect(view().name("design"))
        .andExpect(model().attribute("wrap", ingredients.subList(0, 2)))
        .andExpect(model().attribute("protein", ingredients.subList(2, 4)))
        .andExpect(model().attribute("veggies", ingredients.subList(4, 6)))
        .andExpect(model().attribute("cheese", ingredients.subList(6, 8)))
        .andExpect(model().attribute("sauce", ingredients.subList(8, 10)));
  }

  @Test
  @WithMockUser(username="testuser", password="testpass", authorities="ROLE_USER")
  public void processTaco() throws Exception {
    mockMvc.perform(post("/design")
        .content("name=Test+Taco&ingredients=FLTO,GRBF,CHED")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .with(SecurityMockMvcRequestPostProcessors.csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(header().stringValues("Location", "/orders/current"));
  }

}
