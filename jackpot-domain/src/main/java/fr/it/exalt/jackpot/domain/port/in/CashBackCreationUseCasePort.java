package fr.it.exalt.jackpot.domain.port.in;

import fr.it.exalt.jackpot.domain.model.Jackpot;
import fr.it.exalt.jackpot.domain.model.exception.MailAddressAlreadyUsedException;
import fr.it.exalt.jackpot.domain.model.exception.NegativeAmountNotAllowedException;

import java.math.BigDecimal;

public interface CashBackCreationUseCasePort {

    Jackpot create(BigDecimal amount, String email) throws NegativeAmountNotAllowedException, MailAddressAlreadyUsedException;
}
