package fr.it.exalt.controller.dto.requests;

import java.math.BigDecimal;
import java.util.UUID;

public record JackpotDepositRequest(UUID customerId, BigDecimal amount) {
}
