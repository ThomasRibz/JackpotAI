package fr.it.exalt.infrastructure.adapter;

import fr.it.exalt.infrastructure.message.CashBackIsAvailableMessage;
import fr.it.exalt.jackpot.domain.model.NotificationJackpotIsAvailableContent;
import fr.it.exalt.jackpot.domain.port.out.CustomerNotifierPort;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public class CustomerNotifierAdapter implements CustomerNotifierPort {

    private final KafkaTemplate<String, CashBackIsAvailableMessage> kafkaTemplate;

    @Override
    public void notifyCustomerJackpotAvailability(NotificationJackpotIsAvailableContent notificationJackpotIsAvailableContent) {
        kafkaTemplate.send(
                "jackpot",
                notificationJackpotIsAvailableContent.email(),
                CashBackIsAvailableMessage.from(notificationJackpotIsAvailableContent)
        );
    }
}
