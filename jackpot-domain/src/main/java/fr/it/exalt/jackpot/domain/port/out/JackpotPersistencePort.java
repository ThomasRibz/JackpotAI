package fr.it.exalt.jackpot.domain.port.out;

import fr.it.exalt.jackpot.domain.model.Jackpot;

import java.util.Optional;
import java.util.UUID;

public interface JackpotPersistencePort {

    Optional<Jackpot> getJackpotById(UUID customerId);

    Jackpot persistJackpot(Jackpot jackpot);

    boolean emailAlreadyExists(String email);
}
