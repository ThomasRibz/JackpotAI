package fr.it.exalt.infrastructure.adapter;

import fr.it.exalt.infrastructure.entity.JackpotEntity;
import fr.it.exalt.infrastructure.repository.JackpotRepository;
import fr.it.exalt.jackpot.domain.model.Jackpot;
import fr.it.exalt.jackpot.domain.port.out.JackpotPersistencePort;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class JackpotPersistenceAdapter implements JackpotPersistencePort {

    private final JackpotRepository jackpotRepository;

    @Override
    public Optional<Jackpot> getJackpotById(UUID customerId) {
        return jackpotRepository.findById(customerId).map(JackpotEntity::toDomain);
    }

    @Override
    public Jackpot persistJackpot(Jackpot jackpot) {
        return jackpotRepository.save(JackpotEntity.toEntity(jackpot)).toDomain();
    }

    @Override
    public boolean emailAlreadyExists(String email) {
        return jackpotRepository.existsByEmail(email);
    }
}
