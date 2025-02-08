package fr.it.exalt.controller;

import fr.it.exalt.controller.dto.requests.JackpotDepositRequest;
import fr.it.exalt.controller.dto.requests.JackpotCreationRequest;
import fr.it.exalt.jackpot.domain.model.Jackpot;
import fr.it.exalt.jackpot.domain.model.exception.JackpotNotFoundException;
import fr.it.exalt.jackpot.domain.model.exception.MailAddressAlreadyUsedException;
import fr.it.exalt.jackpot.domain.model.exception.NegativeAmountNotAllowedException;
import fr.it.exalt.jackpot.domain.port.in.CashBackCreationUseCasePort;
import fr.it.exalt.jackpot.domain.port.in.CashBackDepositUseCasePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JackpotControllerAdapterTest {

    public static final String EMAIL = "foo@bar.com";
    @Mock
    CashBackCreationUseCasePort cashBackCreationUseCasePort;
    @Mock
    CashBackDepositUseCasePort cashBackDepositUseCasePort;
    JackpotControllerAdapter controller;

    @BeforeEach
    void setUp() {
        controller = new JackpotControllerAdapter(cashBackCreationUseCasePort, cashBackDepositUseCasePort);
    }

    @Test
    void should_create_jackpot() throws MailAddressAlreadyUsedException, NegativeAmountNotAllowedException {
        //given
        BigDecimal amount = BigDecimal.ONE;
        JackpotCreationRequest request = new JackpotCreationRequest(amount, EMAIL);
        UUID customerId = UUID.randomUUID();
        when(cashBackCreationUseCasePort.create(amount, EMAIL)).thenReturn(new Jackpot(customerId, BigDecimal.ZERO, 1, EMAIL));
        //when
        var response = controller.createJackpot(request);
        //then
        verify(cashBackCreationUseCasePort).create(amount, EMAIL);
        assertThat(Objects.requireNonNull(response.getBody()).customerId()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void should_return_400_when_mail_already_exist_jackpot() throws MailAddressAlreadyUsedException, NegativeAmountNotAllowedException {
        //given
        BigDecimal amount = BigDecimal.ONE;
        JackpotCreationRequest request = new JackpotCreationRequest(amount, EMAIL);
        when(cashBackCreationUseCasePort.create(amount, EMAIL)).thenThrow(new MailAddressAlreadyUsedException(EMAIL));
        //when
        var response = controller.createJackpot(request);
        //then
        verify(cashBackCreationUseCasePort).create(amount, EMAIL);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void should_return_400_when_amount_is_not_valid_when_creating_jackpot() throws
            MailAddressAlreadyUsedException,
            NegativeAmountNotAllowedException {
        //given
        BigDecimal invalidAmount = BigDecimal.valueOf(-1);
        JackpotCreationRequest request = new JackpotCreationRequest(invalidAmount, EMAIL);
        when(cashBackCreationUseCasePort.create(invalidAmount, EMAIL)).thenThrow(new NegativeAmountNotAllowedException(invalidAmount));
        //when
        var response = controller.createJackpot(request);
        //then
        verify(cashBackCreationUseCasePort).create(invalidAmount, EMAIL);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void should_deposit_cash_back_on_jackpot() throws NegativeAmountNotAllowedException, JackpotNotFoundException {
        //given
        BigDecimal amount = BigDecimal.ONE;
        UUID customerId = UUID.randomUUID();
        JackpotDepositRequest request = new JackpotDepositRequest(customerId, amount);
        when(cashBackDepositUseCasePort.deposit(customerId, amount)).thenReturn(new Jackpot(customerId, BigDecimal.ZERO, 1, EMAIL));
        //when
        var response = controller.depositCashBackOnJackpot(request);
        //then
        verify(cashBackDepositUseCasePort).deposit(customerId, amount);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void should_return_400_when_amount_is_not_valid_when_deposit_on_jackpot() throws NegativeAmountNotAllowedException, JackpotNotFoundException {
        //given
        BigDecimal invalidAmount = BigDecimal.valueOf(-1);
        UUID customerId = UUID.randomUUID();
        JackpotDepositRequest request = new JackpotDepositRequest(customerId, invalidAmount);
        when(cashBackDepositUseCasePort.deposit(customerId, invalidAmount)).thenThrow(new NegativeAmountNotAllowedException(invalidAmount));
        //when
        var response = controller.depositCashBackOnJackpot(request);
        //then
        verify(cashBackDepositUseCasePort).deposit(customerId, invalidAmount);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void should_return_404_when_jackpot_is_not_found() throws NegativeAmountNotAllowedException,
            JackpotNotFoundException {
        //given
        BigDecimal amount = BigDecimal.TEN;
        UUID customerId = UUID.randomUUID();
        JackpotDepositRequest request = new JackpotDepositRequest(customerId, amount);
        when(cashBackDepositUseCasePort.deposit(customerId, amount)).thenThrow(new JackpotNotFoundException(customerId));
        //when
        var response = controller.depositCashBackOnJackpot(request);
        //then
        verify(cashBackDepositUseCasePort).deposit(customerId, amount);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
