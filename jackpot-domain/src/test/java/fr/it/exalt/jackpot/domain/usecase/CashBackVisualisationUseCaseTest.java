package fr.it.exalt.jackpot.domain.usecase;

import fr.it.exalt.jackpot.domain.model.CashBackVisualisation;
import fr.it.exalt.jackpot.domain.model.Jackpot;
import fr.it.exalt.jackpot.domain.model.exception.JackpotNotFoundException;
import fr.it.exalt.jackpot.domain.port.in.CashBackVisualisationUseCasePort;
import fr.it.exalt.jackpot.domain.port.out.JackpotPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static fr.it.exalt.jackpot.domain.usecase.CashBackDepositUseCaseTest.EMAIL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CashBackVisualisationUseCaseTest {

    @Mock
    JackpotPersistencePort jackpotPersistencePort;

    public static Stream<Arguments> provideJackpots() {
        return Stream.of(
                Arguments.of(new Jackpot(UUID.randomUUID(), BigDecimal.ZERO, 1, EMAIL), false),
                Arguments.of(new Jackpot(UUID.randomUUID(), BigDecimal.TEN, 1, EMAIL), false),
                Arguments.of(new Jackpot(UUID.randomUUID(), BigDecimal.ZERO, 3, EMAIL), false),
                Arguments.of(new Jackpot(UUID.randomUUID(), BigDecimal.TEN, 3, EMAIL), false),
                Arguments.of(new Jackpot(UUID.randomUUID(), BigDecimal.ZERO, 4, EMAIL), false),
                Arguments.of(new Jackpot(UUID.randomUUID(), BigDecimal.TEN, 4, EMAIL), true)
        );
    }

    @Test
    void should_throw_when_customer_id_does_not_exist() {
        CashBackVisualisationUseCasePort cashBackVisualisationUseCasePort = new CashBackVisualisationUseCase(jackpotPersistencePort);
        UUID customerId = UUID.randomUUID();
        when(jackpotPersistencePort.getJackpotById(customerId)).thenReturn(Optional.empty());
        assertThatExceptionOfType(JackpotNotFoundException.class).isThrownBy(() -> cashBackVisualisationUseCasePort.visualizeCashBack(
                customerId
        ));
    }

    @ParameterizedTest
    @MethodSource("provideJackpots")
    void should_display_cash_back_availability_when_customer_asks_for_it(Jackpot jackpot, boolean expectedAvailability) throws
            JackpotNotFoundException {
        CashBackVisualisationUseCasePort cashBackVisualisationUseCasePort = new CashBackVisualisationUseCase(jackpotPersistencePort);
        UUID customerId = UUID.randomUUID();
        when(jackpotPersistencePort.getJackpotById(customerId)).thenReturn(Optional.of(jackpot));
        CashBackVisualisation cashBackVisualisation = cashBackVisualisationUseCasePort.visualizeCashBack(customerId);
        assertThat(cashBackVisualisation.availability()).isEqualTo(expectedAvailability);
        assertThat(cashBackVisualisation.amount()).isEqualTo(jackpot.amount());
    }
}
