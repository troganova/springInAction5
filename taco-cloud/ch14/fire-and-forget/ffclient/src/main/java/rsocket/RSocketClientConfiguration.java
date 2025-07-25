package rsocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;

import java.time.Instant;

@Slf4j
@Configuration
public class RSocketClientConfiguration {
    @Bean
    public ApplicationRunner sender(RSocketRequester.Builder requesterBuilder) {
        return args -> {
            RSocketRequester tcp = requesterBuilder.tcp("localhost", 7001);
            tcp
                    .route("alert")
                    .data(new Alert(
                            Alert.Level.RED, "Craig", Instant.now()))
                    .send()
                    .subscribe();
            log.info("Alert sent");

            Thread.sleep(10000);
        };
    }
}
