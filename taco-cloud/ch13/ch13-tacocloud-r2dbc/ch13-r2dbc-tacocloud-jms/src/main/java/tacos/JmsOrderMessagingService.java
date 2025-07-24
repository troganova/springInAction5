package tacos;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JmsOrderMessagingService implements OrderMessagingService {

  private final JmsTemplate jms;

  @Override
  public void sendOrder(TacoOrder order) {
    jms.convertAndSend("tacocloud.order.queue", order, this::addOrderSource);
  }

//  @Override
//  public void sendOrder(TacoOrder order) {
//    jms.send(session -> session.createObjectMessage(order));
//  }
//
  private Message addOrderSource(Message message) throws JMSException {
    message.setStringProperty("X_ORDER_SOURCE", "WEB");
    return message;
  }

}
