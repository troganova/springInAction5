package tacos.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mail.ImapMailReceiver;
import org.springframework.integration.mail.MailReceivingMessageSource;
import org.springframework.integration.transformer.MessageTransformingHandler;
import org.springframework.messaging.MessageHandler;
import tacos.config.EmailProperties;
import tacos.entity.EmailOrder;

@Configuration
public class TacoOrderEmailIntegrationConfig {

    @Bean
    public DirectChannel tacoOrderChannel() {
      return new DirectChannel();
    }

    @Bean
    public DirectChannel orderProcessingChannel() {
      return new DirectChannel();
    }

    @Bean
    public ImapMailReceiver mailReceiver(EmailProperties emailProps) {
      ImapMailReceiver mailReceiver = new ImapMailReceiver(emailProps.getImapUrl());
      mailReceiver.setShouldDeleteMessages(false);
      mailReceiver.setShouldMarkMessagesAsRead(true);
      return mailReceiver;
    }

    @Bean
    @InboundChannelAdapter(value = "tacoOrderChannel", poller = @Poller(fixedDelay = "${tacocloud.email.poll-rate}"))
    public MailReceivingMessageSource mailMessageSource(ImapMailReceiver mailReceiver) {
      return new MailReceivingMessageSource(mailReceiver);
    }

    @Bean
    @Transformer(inputChannel = "tacoOrderChannel", outputChannel = "orderProcessingChannel")
    public MessageHandler emailOrderTransformer(EmailToOrderTransformer transformer) {
      return new MessageTransformingHandler(transformer::transform);
    }

    @Bean
    @ServiceActivator(inputChannel = "orderProcessingChannel")
    public MessageHandler orderSubmitHandler(OrderSubmitMessageHandler handler) {
        return message -> {
            EmailOrder payload = (EmailOrder) message.getPayload();
            handler.handle(payload, message.getHeaders());
        };
    }

  
//  @Bean
//  public IntegrationFlow tacoOrderEmailFlow(
//      EmailProperties emailProps,
//      EmailToOrderTransformer emailToOrderTransformer,
//      OrderSubmitMessageHandler orderSubmitHandler) {
//
//    return IntegrationFlows
//        .from(Mail.imapInboundAdapter(emailProps.getImapUrl()),
//            e -> e.poller(
//                Pollers.fixedDelay(emailProps.getPollRate())))
//        .transform(emailToOrderTransformer)
//        .handle(orderSubmitHandler)
//        .get();
//  }
  
}