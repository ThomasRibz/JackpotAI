package fr.it.exalt.controller.dto.requests;

import java.math.BigDecimal;

public record JackpotCreationRequest(BigDecimal amount, String email) {

}
