package tacos.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;
import tacos.repository.IngredientRepository;
import tacos.repository.OrderRepository;
import tacos.repository.UserRepository;
import tacos.security.SecurityConfig;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@Import(SecurityConfig.class)
public class HomeControllerTest {

  @Autowired
  private MockMvc mockMvc;   // <2>

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
    public UserRepository userRepository() {
      return Mockito.mock(UserRepository.class);
    }
  }

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private IngredientRepository ingredientRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void testHomePage() throws Exception {
    mockMvc.perform(get("/"))    // <3>
    
      .andExpect(status().isOk())  // <4>
      
      .andExpect(view().name("home"))  // <5>
      
      .andExpect(content().string(           // <6>
          containsString("Welcome to...")));  
  }

}
