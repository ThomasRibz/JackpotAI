package fr.it.exalt.jackpot.domain.model;

import java.math.BigDecimal;

public record CashBackVisualisation(boolean availability, BigDecimal amount) {

    public static CashBackVisualisation from(Jackpot jackpot) {
        return new CashBackVisualisation(jackpot.isAvailable(), jackpot.amount());
    }
}
