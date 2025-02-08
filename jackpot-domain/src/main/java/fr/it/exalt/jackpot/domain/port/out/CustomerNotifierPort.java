package fr.it.exalt.jackpot.domain.port.out;

import fr.it.exalt.jackpot.domain.model.NotificationJackpotIsAvailableContent;

public interface CustomerNotifierPort {

    void notifyCustomerJackpotAvailability(NotificationJackpotIsAvailableContent capture);
}
