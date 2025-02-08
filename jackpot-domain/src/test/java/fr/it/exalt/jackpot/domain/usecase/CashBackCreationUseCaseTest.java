package fr.it.exalt.jackpot.domain.usecase;

import fr.it.exalt.jackpot.domain.model.Jackpot;
import fr.it.exalt.jackpot.domain.model.exception.MailAddressAlreadyUsedException;
import fr.it.exalt.jackpot.domain.model.exception.NegativeAmountNotAllowedException;
import fr.it.exalt.jackpot.domain.port.out.JackpotPersistencePort;
import fr.it.exalt.jackpot.infrastructure.adapter.DummyJackpotPersistenceAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
public class CashBackCreationUseCaseTest {

    public static final String ALREADY_EXISTING_EMAIL = "bar@foo.com";
    JackpotPersistencePort jackpotPersistencePort = new DummyJackpotPersistenceAdapter();

    @Test
    void should_create_Jackpot() throws NegativeAmountNotAllowedException, MailAddressAlreadyUsedException {
        CashBackCreationUseCase cashBackCreationUseCase = new CashBackCreationUseCase(jackpotPersistencePort);
        String email = "foo@bar.com";
        BigDecimal amount = BigDecimal.ONE;
        Jackpot jackpot = cashBackCreationUseCase.create(amount, email);
        assertThat(jackpot.amount()).isEqualTo(amount);
        assertThat(jackpot.email()).isEqualTo(email);
    }

    @Test
    void should_throw_when_using_already_existing_email_on_Jackpot_creation() {
        CashBackCreationUseCase cashBackCreationUseCase = new CashBackCreationUseCase(jackpotPersistencePort);
        assertThatExceptionOfType(MailAddressAlreadyUsedException.class).isThrownBy(() -> cashBackCreationUseCase.create(
                BigDecimal.ONE,
                ALREADY_EXISTING_EMAIL
        ));
    }
}
