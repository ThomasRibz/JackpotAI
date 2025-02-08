package fr.it.exalt.jackpot.domain.usecase;

import fr.it.exalt.jackpot.domain.model.Jackpot;
import fr.it.exalt.jackpot.domain.model.NotificationJackpotIsAvailableContent;
import fr.it.exalt.jackpot.domain.port.in.NotifyCashBackIsAvailableUseCasePort;
import fr.it.exalt.jackpot.domain.port.out.CustomerNotifierPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NotifyCashBackIsAvailableUseCase implements NotifyCashBackIsAvailableUseCasePort {

    private final CustomerNotifierPort customerNotifierPort;

    @Override
    public void notifyIfJackpotAvailable(Jackpot jackpot) {
        if (jackpot.isAvailable()) {
            customerNotifierPort.notifyCustomerJackpotAvailability(NotificationJackpotIsAvailableContent.from(jackpot));
        }
    }
}
