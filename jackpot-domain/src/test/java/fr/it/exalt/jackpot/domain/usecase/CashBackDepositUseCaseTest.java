package fr.it.exalt.jackpot.domain.usecase;

import fr.it.exalt.jackpot.domain.model.Jackpot;
import fr.it.exalt.jackpot.domain.model.exception.JackpotNotFoundException;
import fr.it.exalt.jackpot.domain.model.exception.NegativeAmountNotAllowedException;
import fr.it.exalt.jackpot.domain.port.in.NotifyCashBackIsAvailableUseCasePort;
import fr.it.exalt.jackpot.domain.port.out.JackpotPersistencePort;
import fr.it.exalt.jackpot.infrastructure.adapter.DummyJackpotPersistenceAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CashBackDepositUseCaseTest {

    public static UUID UNKNOWN_CUSTOMER_ID = UUID.randomUUID();
    public static String EMAIL = "foo@bar.com";

    JackpotPersistencePort jackpotPersistencePort = new DummyJackpotPersistenceAdapter();
    @Mock
    NotifyCashBackIsAvailableUseCasePort notifyCashBackIsAvailableUseCasePort;
    CashBackDepositUseCase cashBackDepositUseCase;

    @BeforeEach
    void setUp() {
        cashBackDepositUseCase = new CashBackDepositUseCase(jackpotPersistencePort, notifyCashBackIsAvailableUseCasePort);
    }

    public static Stream<Arguments> provideAmounts() {
        return Stream.of(Arguments.of(BigDecimal.ONE), Arguments.of(BigDecimal.valueOf(3)));
    }

    @ParameterizedTest
    @MethodSource("provideAmounts")
    void should_update_Jackpot(BigDecimal amount) throws NegativeAmountNotAllowedException, JackpotNotFoundException {
        UUID customerId = UUID.randomUUID();
        Jackpot jackpot = cashBackDepositUseCase.deposit(customerId, amount);
        assertThat(jackpot.amount()).isEqualTo(BigDecimal.ONE.add(amount));
    }

    @Test
    void should_throw_when_negative_amount_to_add_on_Jackpot() {
        assertThatExceptionOfType(NegativeAmountNotAllowedException.class).isThrownBy(() -> cashBackDepositUseCase.deposit(
                UUID.randomUUID(),
                BigDecimal.valueOf(-1)
        ));
    }

    @Test
    void should_throw_when_unknown_customer_Jackpot() {
        assertThatExceptionOfType(JackpotNotFoundException.class).isThrownBy(() -> cashBackDepositUseCase.deposit(
                UNKNOWN_CUSTOMER_ID,
                BigDecimal.TEN
        ));
    }

    @Test
    void should_notify_when_Jackpot_becomes_available() throws NegativeAmountNotAllowedException, JackpotNotFoundException {
        UUID customerId = UUID.randomUUID();
        cashBackDepositUseCase.deposit(customerId, BigDecimal.TEN);
        verify(notifyCashBackIsAvailableUseCasePort).notifyIfJackpotAvailable(any());
    }
}
