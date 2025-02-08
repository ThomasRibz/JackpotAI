package fr.it.exalt.jackpot.domain.usecase;

import fr.it.exalt.jackpot.domain.model.Jackpot;
import fr.it.exalt.jackpot.domain.model.NotificationJackpotIsAvailableContent;
import fr.it.exalt.jackpot.domain.port.in.NotifyCashBackIsAvailableUseCasePort;
import fr.it.exalt.jackpot.domain.port.out.CustomerNotifierPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;

import static fr.it.exalt.jackpot.domain.usecase.CashBackDepositUseCase.DEFAULT_CHECKOUT_NUMBER;
import static fr.it.exalt.jackpot.domain.usecase.CashBackDepositUseCaseTest.EMAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class NotifyCashBackIsAvailableUseCaseTest {

    @Mock
    CustomerNotifierPort customerNotifierPort;

    public static Stream<Arguments> provideJackpotValues() {
        return Stream.of(
                Arguments.of(BigDecimal.valueOf(123.45), BigDecimal.valueOf(120)),
                Arguments.of(BigDecimal.valueOf(19.99), BigDecimal.TEN),
                Arguments.of(BigDecimal.valueOf(10), BigDecimal.TEN),
                Arguments.of(BigDecimal.valueOf(10.0001), BigDecimal.TEN)
        );
    }

    @Test
    void should_notify_when_jackpot_is_available_in_Jackpot() {
        ArgumentCaptor<NotificationJackpotIsAvailableContent> notificationContentCaptor = ArgumentCaptor.forClass(
                NotificationJackpotIsAvailableContent.class);
        NotifyCashBackIsAvailableUseCasePort notifyCashBackIsAvailableUseCasePort = new NotifyCashBackIsAvailableUseCase(customerNotifierPort);
        UUID customerId = UUID.randomUUID();
        Jackpot availableJackpot = new Jackpot(customerId, BigDecimal.TEN, 5, EMAIL);
        notifyCashBackIsAvailableUseCasePort.notifyIfJackpotAvailable(availableJackpot);
        verify(customerNotifierPort).notifyCustomerJackpotAvailability(notificationContentCaptor.capture());
        NotificationJackpotIsAvailableContent notificationContent = notificationContentCaptor.getValue();
        assertThat(notificationContent.amount()).isEqualTo(availableJackpot.amount());
        assertThat(notificationContent.email()).isEqualTo(availableJackpot.email());
    }

    @Test
    void should_not_notify_when_jackpot_is_not_available_in_Jackpot() {
        NotifyCashBackIsAvailableUseCasePort notifyCashBackIsAvailableUseCasePort = new NotifyCashBackIsAvailableUseCase(customerNotifierPort);
        UUID customerId = UUID.randomUUID();
        notifyCashBackIsAvailableUseCasePort.notifyIfJackpotAvailable(new Jackpot(customerId, BigDecimal.ZERO, DEFAULT_CHECKOUT_NUMBER, EMAIL));
        verify(customerNotifierPort, Mockito.never()).notifyCustomerJackpotAvailability(any());
    }

    @ParameterizedTest
    @MethodSource("provideJackpotValues")
    void should_notify_with_correct_amount_when_jackpot_is_available_in_Jackpot(BigDecimal jackpotValue, BigDecimal availableAmount) {
        ArgumentCaptor<NotificationJackpotIsAvailableContent> captor = ArgumentCaptor.forClass(NotificationJackpotIsAvailableContent.class);
        NotifyCashBackIsAvailableUseCasePort notifyCashBackIsAvailableUseCasePort = new NotifyCashBackIsAvailableUseCase(customerNotifierPort);
        UUID customerId = UUID.randomUUID();
        notifyCashBackIsAvailableUseCasePort.notifyIfJackpotAvailable(new Jackpot(customerId, jackpotValue, 5, EMAIL));
        verify(customerNotifierPort).notifyCustomerJackpotAvailability(captor.capture());
        var notificationContent = captor.getValue();
        assertThat(notificationContent.amount()).isEqualTo(availableAmount);

    }
}
