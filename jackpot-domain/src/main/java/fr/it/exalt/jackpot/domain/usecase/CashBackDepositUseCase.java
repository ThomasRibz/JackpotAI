package fr.it.exalt.jackpot.domain.usecase;

import fr.it.exalt.jackpot.domain.model.Jackpot;
import fr.it.exalt.jackpot.domain.model.exception.JackpotNotFoundException;
import fr.it.exalt.jackpot.domain.model.exception.NegativeAmountNotAllowedException;
import fr.it.exalt.jackpot.domain.port.in.CashBackDepositUseCasePort;
import fr.it.exalt.jackpot.domain.port.in.NotifyCashBackIsAvailableUseCasePort;
import fr.it.exalt.jackpot.domain.port.out.JackpotPersistencePort;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
public class CashBackDepositUseCase implements CashBackDepositUseCasePort {

    public static final int DEFAULT_CHECKOUT_NUMBER = 0;
    private final JackpotPersistencePort jackpotPersistencePort;
    private final NotifyCashBackIsAvailableUseCasePort notifyCashBackIsAvailableUseCasePort;

    @Override
    public Jackpot deposit(UUID customerId, BigDecimal amount) throws NegativeAmountNotAllowedException, JackpotNotFoundException {
        Jackpot jackpot = jackpotPersistencePort.getJackpotById(customerId)
                                                .orElseThrow(() -> new JackpotNotFoundException(customerId));
        Jackpot jackpotUpdated = jackpot.addAmount(amount);
        notifyCashBackIsAvailableUseCasePort.notifyIfJackpotAvailable(jackpotUpdated);
        return jackpotPersistencePort.persistJackpot(jackpotUpdated);
    }
}
