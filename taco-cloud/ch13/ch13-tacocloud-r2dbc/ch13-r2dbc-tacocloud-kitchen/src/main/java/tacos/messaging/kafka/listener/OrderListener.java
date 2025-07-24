package tacos.messaging.kafka.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import tacos.domain.TacoOrder;
import tacos.kitchen.KitchenUI;

@Profile("kafka-listener")
@Component
@Slf4j
@RequiredArgsConstructor
public class OrderListener {

  private final KitchenUI ui;

  @KafkaListener(topics="tacocloud.orders.topic", groupId = "tacocloud_kitchen")
  public void handle(@Payload TacoOrder order, ConsumerRecord<String, TacoOrder> record) {
    log.info("Received from partition {} with timestamp {}", record.partition(), record.timestamp());

    ui.displayOrder(order);
  }
}
