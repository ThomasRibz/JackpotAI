package fr.it.exalt.jackpot.domain.port.in;

import fr.it.exalt.jackpot.domain.model.Jackpot;

@FunctionalInterface
public interface NotifyCashBackIsAvailableUseCasePort {

    void notifyIfJackpotAvailable(Jackpot jackpot);
}
