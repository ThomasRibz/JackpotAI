package fr.it.exalt.jackpot.domain.model.exception;

import java.io.Serial;
import java.math.BigDecimal;

public class NegativeAmountNotAllowedException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final String MESSAGE = "Negative amount (%s) not allowed";

    public NegativeAmountNotAllowedException(BigDecimal amount) {
        super(MESSAGE.formatted(amount));
    }
}
