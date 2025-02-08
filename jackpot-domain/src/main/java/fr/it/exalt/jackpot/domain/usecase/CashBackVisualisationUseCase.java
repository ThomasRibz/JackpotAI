package fr.it.exalt.jackpot.domain.usecase;

import fr.it.exalt.jackpot.domain.model.CashBackVisualisation;
import fr.it.exalt.jackpot.domain.model.Jackpot;
import fr.it.exalt.jackpot.domain.model.exception.JackpotNotFoundException;
import fr.it.exalt.jackpot.domain.port.in.CashBackVisualisationUseCasePort;
import fr.it.exalt.jackpot.domain.port.out.JackpotPersistencePort;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class CashBackVisualisationUseCase implements CashBackVisualisationUseCasePort {

    private final JackpotPersistencePort jackpotPersistencePort;

    @Override
    public CashBackVisualisation visualizeCashBack(UUID customerId) throws JackpotNotFoundException {
        Jackpot jackpot = jackpotPersistencePort.getJackpotById(customerId).orElseThrow(() -> new JackpotNotFoundException(customerId));
        return CashBackVisualisation.from(jackpot);
    }
}
