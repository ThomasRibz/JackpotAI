package fr.it.exalt.infrastructure.adapter;

import fr.it.exalt.infrastructure.entity.JackpotEntity;
import fr.it.exalt.infrastructure.repository.JackpotRepository;
import fr.it.exalt.jackpot.domain.model.Jackpot;
import fr.it.exalt.jackpot.domain.port.out.JackpotPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class JackpotPersistenceAdapterTest {

    private static final String ALREADY_EXISTING_EMAIL = "foo@bar.com";
    private static final String EMAIL = "bar@foo.com";
    @Autowired
    private JackpotRepository jackpotRepository;
    private JackpotPersistencePort port;

    @BeforeEach
    void setUp() {
        port = new JackpotPersistenceAdapter(jackpotRepository);
    }

    @Test
    void should_return_correct_boolean_when_checking_email_existence() {
        //given
        JackpotEntity jackpotEntity =
                JackpotEntity.builder().customerId(UUID.randomUUID()).email(ALREADY_EXISTING_EMAIL).checkoutNumber(1).amount(BigDecimal.TEN).build();
        jackpotRepository.save(jackpotEntity);
        //when
        //then
        assertThat(port.emailAlreadyExists(ALREADY_EXISTING_EMAIL)).isTrue();
        assertThat(port.emailAlreadyExists(EMAIL)).isFalse();
    }

    @Test
    void should_return_jackpot_when_asking_it() {
        //given
        UUID customerId = UUID.randomUUID();
        JackpotEntity jackpotEntity = new JackpotEntity(customerId, BigDecimal.ONE, 1, ALREADY_EXISTING_EMAIL);
        jackpotRepository.save(jackpotEntity);
        //when
        //then
        assertThat(port.getJackpotById(customerId).get()).isEqualTo(jackpotEntity.toDomain());
        assertThat(port.getJackpotById(UUID.randomUUID())).isEmpty();
    }

    @Test
    void should_save_jackpot_when_persisting_it() {
        //given
        UUID customerId = UUID.randomUUID();
        JackpotEntity jackpotEntity = new JackpotEntity(customerId, BigDecimal.ONE, 1, ALREADY_EXISTING_EMAIL);
        jackpotRepository.save(jackpotEntity);
        //when
        Jackpot jackpot = new Jackpot(customerId, BigDecimal.TWO, 2, ALREADY_EXISTING_EMAIL);
        port.persistJackpot(jackpot);

        //then
        assertThat(jackpotRepository.findById(customerId).get().toDomain()).isEqualTo(jackpot);
        assertThat(port.getJackpotById(UUID.randomUUID())).isEmpty();
    }
}
