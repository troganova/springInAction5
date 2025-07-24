package tacos.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HomeControllerTest {

  @Autowired
  private WebTestClient testClient;

  @Test
  public void testHomePage() throws Exception {
    testClient.get().uri("/")   // <3>

            .exchange().expectStatus().isOk() // <4>

            .expectBody(String.class)
            .consumeWith(response -> {
              assertThat(response.getResponseBody())
                      .contains("Welcome to...");
            });
  }

}
