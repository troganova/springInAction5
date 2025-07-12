package tacos.messaging.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({"rabbitmq-template", "rabbitmq-listener"})
@Configuration
public class MessagingConfig {

  @Bean
  public Jackson2JsonMessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public org.springframework.amqp.core.Queue orderQueue() {
    return QueueBuilder.durable("tacocloud.order.queue")
            .build();
  }

  @Bean
  public DirectExchange orderExchange() {
    return new DirectExchange("tacocloud.order.exchange");
  }

  @Bean
  public Binding binding(org.springframework.amqp.core.Queue orderQueue, DirectExchange orderExchange) {
    return BindingBuilder.bind(orderQueue)
            .to(orderExchange)
            .with("tacocloud.order.routingkey");
  }

}