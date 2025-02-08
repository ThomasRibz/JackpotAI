package fr.it.exalt.jackpot.domain.model;

import fr.it.exalt.jackpot.domain.model.exception.NegativeAmountNotAllowedException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public record Jackpot(UUID customerId, BigDecimal amount, int checkoutNumber, String email) {

    public Jackpot addAmount(BigDecimal amount) throws NegativeAmountNotAllowedException {
        checkAmountValidity(amount);
        return new Jackpot(customerId, this.amount.add(amount), checkoutNumber + 1, email);
    }

    private static void checkAmountValidity(BigDecimal amount) throws NegativeAmountNotAllowedException {
        if (amount.signum() < 0) {
            throw new NegativeAmountNotAllowedException(amount);
        }
    }

    public boolean isAvailable() {
        return this.checkoutNumber() > 3 && this.amount().compareTo(BigDecimal.TEN) >= 0;
    }

    public BigDecimal computeAvailableAmount() {
        return this.amount().divideToIntegralValue(BigDecimal.TEN).multiply(BigDecimal.TEN).setScale(0, RoundingMode.HALF_UP);
    }
}
