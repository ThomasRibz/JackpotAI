package fr.it.exalt.configuration.bean;

import fr.it.exalt.infrastructure.adapter.CustomerNotifierAdapter;
import fr.it.exalt.infrastructure.adapter.JackpotPersistenceAdapter;
import fr.it.exalt.infrastructure.message.CashBackIsAvailableMessage;
import fr.it.exalt.infrastructure.repository.JackpotRepository;
import fr.it.exalt.jackpot.domain.port.in.CashBackCreationUseCasePort;
import fr.it.exalt.jackpot.domain.port.in.CashBackDepositUseCasePort;
import fr.it.exalt.jackpot.domain.port.in.NotifyCashBackIsAvailableUseCasePort;
import fr.it.exalt.jackpot.domain.port.out.CustomerNotifierPort;
import fr.it.exalt.jackpot.domain.port.out.JackpotPersistencePort;
import fr.it.exalt.jackpot.domain.usecase.CashBackCreationUseCase;
import fr.it.exalt.jackpot.domain.usecase.CashBackDepositUseCase;
import fr.it.exalt.jackpot.domain.usecase.NotifyCashBackIsAvailableUseCase;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@AllArgsConstructor
@EnableJpaRepositories(basePackages = "fr.it.exalt")
public class BeanConfiguration {

    JackpotRepository jackpotRepository;
    KafkaTemplate<String, CashBackIsAvailableMessage> kafkaTemplate;

    @Bean
    CustomerNotifierPort customerNotifierPort() {
        return new CustomerNotifierAdapter(kafkaTemplate);
    }

    @Bean
    JackpotPersistencePort jackpotPersistencePort() {
        return new JackpotPersistenceAdapter(jackpotRepository);
    }

    @Bean
    NotifyCashBackIsAvailableUseCasePort notifyCashBackIsAvailableUseCasePort(CustomerNotifierPort customerNotifierPort) {
        return new NotifyCashBackIsAvailableUseCase(customerNotifierPort);
    }

    @Bean
    CashBackDepositUseCasePort cashBackDepositUseCasePort(
            JackpotPersistencePort jackpotPersistencePort,
            NotifyCashBackIsAvailableUseCasePort notifyCashBackIsAvailableUseCasePort
    ) {
        return new CashBackDepositUseCase(jackpotPersistencePort, notifyCashBackIsAvailableUseCasePort);
    }

    @Bean
    CashBackCreationUseCasePort cashBackCreationUseCasePort(JackpotPersistencePort jackpotPersistencePort) {
        return new CashBackCreationUseCase(jackpotPersistencePort);
    }
}
