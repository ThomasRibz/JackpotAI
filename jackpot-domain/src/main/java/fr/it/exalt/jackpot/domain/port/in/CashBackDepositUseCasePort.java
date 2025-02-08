package fr.it.exalt.jackpot.domain.port.in;

import fr.it.exalt.jackpot.domain.model.Jackpot;
import fr.it.exalt.jackpot.domain.model.exception.JackpotNotFoundException;
import fr.it.exalt.jackpot.domain.model.exception.NegativeAmountNotAllowedException;

import java.math.BigDecimal;
import java.util.UUID;

public interface CashBackDepositUseCasePort {

    Jackpot deposit(UUID customerId, BigDecimal amount) throws NegativeAmountNotAllowedException, JackpotNotFoundException;
}
