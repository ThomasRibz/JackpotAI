package fr.it.exalt.infrastructure.message;

import fr.it.exalt.jackpot.domain.model.NotificationJackpotIsAvailableContent;

import java.math.BigDecimal;

public record CashBackIsAvailableMessage(String email, BigDecimal amount) {

    public static CashBackIsAvailableMessage from(NotificationJackpotIsAvailableContent notification) {
        return new CashBackIsAvailableMessage(notification.email(), notification.amount());
    }
}
