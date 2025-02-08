package fr.it.exalt.jackpot.infrastructure.adapter;

import fr.it.exalt.jackpot.domain.model.Jackpot;
import fr.it.exalt.jackpot.domain.port.out.JackpotPersistencePort;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static fr.it.exalt.jackpot.domain.usecase.CashBackCreationUseCaseTest.ALREADY_EXISTING_EMAIL;
import static fr.it.exalt.jackpot.domain.usecase.CashBackDepositUseCaseTest.EMAIL;
import static fr.it.exalt.jackpot.domain.usecase.CashBackDepositUseCaseTest.UNKNOWN_CUSTOMER_ID;

public class DummyJackpotPersistenceAdapter implements JackpotPersistencePort {

    @Override
    public Optional<Jackpot> getJackpotById(UUID customerId) {
        return customerId.equals(UNKNOWN_CUSTOMER_ID) ? Optional.empty() : Optional.of(new Jackpot(
                customerId,
                BigDecimal.ONE,
                10,
                EMAIL
        ));
    }

    @Override
    public Jackpot persistJackpot(Jackpot jackpot) {
        return jackpot;
    }

    @Override
    public boolean emailAlreadyExists(String email) {
        return ALREADY_EXISTING_EMAIL.equals(email);
    }
}
