package rsocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;

@Slf4j
@Configuration
public class RSocketClientConfiguration {
    @Bean
    public ApplicationRunner sender(RSocketRequester.Builder requesterBuilder) {
        return args -> {
            RSocketRequester tcp = requesterBuilder.tcp("localhost", 7001);
            tcp
                .route("greeting")
                .data("Hello RSocket!")
                .retrieveMono(String.class)
                .subscribe(response -> log.info("Got a response: {}", response));

            String who = "Craig";
            tcp
                .route("greeting/{name}", who)
                .data("Hello RSocket!")
                .retrieveMono(String.class)
                .subscribe(response -> log.info("Got a response: {}", response));

            Thread.sleep(10000);
        };
    }
}
