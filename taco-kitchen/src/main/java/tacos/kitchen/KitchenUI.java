package tacos.kitchen;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import tacos.Order;

@Slf4j
@Component
public class KitchenUI {

  public void displayOrder(Order order) {
    log.info("RECEIVED ORDER:  " + order);
  }
  
}
