package tacos.kitchen;

import tacos.domain.TacoOrder;

public interface OrderReceiver {

  TacoOrder receiveOrder();

}