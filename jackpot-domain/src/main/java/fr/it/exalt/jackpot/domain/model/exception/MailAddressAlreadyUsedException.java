package fr.it.exalt.jackpot.domain.model.exception;

import java.io.Serial;

public class MailAddressAlreadyUsedException extends Exception {

    @Serial
    private static final long serialVersionUID = 2L;
    public static final String MESSAGE = "Mail address (%s) already used by another customer";

    public MailAddressAlreadyUsedException(String email) {
        super(MESSAGE.formatted(email));
    }
}
