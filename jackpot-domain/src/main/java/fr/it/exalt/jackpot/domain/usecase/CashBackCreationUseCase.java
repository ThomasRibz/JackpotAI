package fr.it.exalt.jackpot.domain.usecase;

import fr.it.exalt.jackpot.domain.model.Jackpot;
import fr.it.exalt.jackpot.domain.model.exception.MailAddressAlreadyUsedException;
import fr.it.exalt.jackpot.domain.model.exception.NegativeAmountNotAllowedException;
import fr.it.exalt.jackpot.domain.port.in.CashBackCreationUseCasePort;
import fr.it.exalt.jackpot.domain.port.out.JackpotPersistencePort;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
public class CashBackCreationUseCase implements CashBackCreationUseCasePort {

    public static final int DEFAULT_CHECKOUT_NUMBER = 0;
    private final JackpotPersistencePort jackpotPersistencePort;

    @Override
    public Jackpot create(BigDecimal amount, String email) throws NegativeAmountNotAllowedException, MailAddressAlreadyUsedException {
        if (emailIsNotAvailable(email)) {
            throw new MailAddressAlreadyUsedException(email);
        }
        Jackpot jackpot = new Jackpot(UUID.randomUUID(), BigDecimal.ZERO, DEFAULT_CHECKOUT_NUMBER, email);
        return jackpotPersistencePort.persistJackpot(jackpot.addAmount(amount));
    }

    private boolean emailIsNotAvailable(String email) {
        return jackpotPersistencePort.emailAlreadyExists(email);
    }
}
