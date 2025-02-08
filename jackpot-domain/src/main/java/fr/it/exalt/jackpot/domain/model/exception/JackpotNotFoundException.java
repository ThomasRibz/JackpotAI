package fr.it.exalt.jackpot.domain.model.exception;

import java.io.Serial;
import java.util.UUID;

public class JackpotNotFoundException extends Exception {

    @Serial
    private static final long serialVersionUID = 2L;
    public static final String MESSAGE = "Jackpot associated to customer (%s) not found";

    public JackpotNotFoundException(UUID customerId) {
        super(MESSAGE.formatted(customerId));
    }
}
