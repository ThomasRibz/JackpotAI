package fr.it.exalt.controller;

import fr.it.exalt.controller.dto.requests.JackpotCreationRequest;
import fr.it.exalt.controller.dto.requests.JackpotDepositRequest;
import fr.it.exalt.controller.dto.responses.JackpotCreatedResponse;
import fr.it.exalt.jackpot.domain.model.Jackpot;
import fr.it.exalt.jackpot.domain.model.exception.JackpotNotFoundException;
import fr.it.exalt.jackpot.domain.model.exception.MailAddressAlreadyUsedException;
import fr.it.exalt.jackpot.domain.model.exception.NegativeAmountNotAllowedException;
import fr.it.exalt.jackpot.domain.port.in.CashBackCreationUseCasePort;
import fr.it.exalt.jackpot.domain.port.in.CashBackDepositUseCasePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("jackpot")
public class JackpotControllerAdapter {

    private final CashBackCreationUseCasePort cashBackCreationUseCasePort;
    private final CashBackDepositUseCasePort cashBackDepositUseCasePort;

    @PostMapping
    public ResponseEntity<JackpotCreatedResponse> createJackpot(@RequestBody JackpotCreationRequest request) {
        Jackpot jackpot;
        try {
            jackpot = cashBackCreationUseCasePort.create(request.amount(), request.email());
        } catch (NegativeAmountNotAllowedException | MailAddressAlreadyUsedException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new JackpotCreatedResponse(jackpot.customerId()));
    }

    @PatchMapping
    public ResponseEntity<Void> depositCashBackOnJackpot(@RequestBody JackpotDepositRequest request) {
        try {
            cashBackDepositUseCasePort.deposit(request.customerId(), request.amount());
        } catch (NegativeAmountNotAllowedException e) {
            return ResponseEntity.badRequest().build();
        } catch (JackpotNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
