package fr.it.exalt.jackpot.domain.port.in;

import fr.it.exalt.jackpot.domain.model.CashBackVisualisation;
import fr.it.exalt.jackpot.domain.model.exception.JackpotNotFoundException;

import java.util.UUID;

public interface CashBackVisualisationUseCasePort {

    CashBackVisualisation visualizeCashBack(UUID customerId) throws JackpotNotFoundException;
}
