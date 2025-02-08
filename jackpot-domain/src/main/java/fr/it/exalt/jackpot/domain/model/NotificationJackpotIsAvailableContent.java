package fr.it.exalt.jackpot.domain.model;

import java.math.BigDecimal;

public record NotificationJackpotIsAvailableContent(BigDecimal amount, String email) {

    public static NotificationJackpotIsAvailableContent from(Jackpot jackpot) {
        return new NotificationJackpotIsAvailableContent(jackpot.computeAvailableAmount(), jackpot.email());
    }
}
