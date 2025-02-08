package fr.it.exalt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import fr.it.exalt.JackpotApplication;
import fr.it.exalt.consumer.KafkaConsumer;
import fr.it.exalt.controller.dto.requests.JackpotDepositRequest;
import fr.it.exalt.infrastructure.entity.JackpotEntity;
import fr.it.exalt.infrastructure.message.CashBackIsAvailableMessage;
import fr.it.exalt.infrastructure.repository.JackpotRepository;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.junit.ClassRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@Import(JackpotControllerAdapterTestContainersIntegrationTest.KafkaTestContainersConfiguration.class)
@SpringBootTest(classes = JackpotApplication.class)
@DirtiesContext
class JackpotControllerAdapterTestContainersIntegrationTest {

    @ClassRule
    public static KafkaContainer kafka =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));

    @Autowired
    JackpotRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private KafkaConsumer consumer;

    private static ObjectWriter ow;
    public static final String ALREADY_EXISTING_EMAIL = "bar@foo.com";

    @BeforeAll
    static void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        ow = mapper.writer().withDefaultPrettyPrinter();
        kafka.start();  // Démarre explicitement le conteneur Kafka
    }

    @DynamicPropertySource
    static void registerKafkaProperties(DynamicPropertyRegistry registry) {
        // Injecte le bootstrap.servers dynamiquement après le démarrage du conteneur
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Test
    void should_notify_cash_back_is_available_on_jackpot() throws Exception {
        // given
        UUID customerId = UUID.randomUUID();
        JackpotEntity jackpotEntity =
                JackpotEntity.builder().customerId(customerId).email(ALREADY_EXISTING_EMAIL).checkoutNumber(4).amount(BigDecimal.TEN).build();
        repository.save(jackpotEntity);
        BigDecimal amount = BigDecimal.ONE;
        JackpotDepositRequest request = new JackpotDepositRequest(customerId, amount);
        String requestJson = ow.writeValueAsString(request);

        // when
        this.mockMvc.perform(patch("/jackpot").content(requestJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

        // then
        boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
        assertThat(messageConsumed).isTrue();
        assertThat(consumer.getPayload()).contains(ALREADY_EXISTING_EMAIL);
    }

    @TestConfiguration
    static class KafkaTestContainersConfiguration {

        @Bean
        public KafkaTemplate<String, CashBackIsAvailableMessage> kafkaTemplate(
                ProducerFactory<String, CashBackIsAvailableMessage> producerFactory
        ) {
            return new KafkaTemplate<>(producerFactory);
        }

        @Bean
        public ProducerFactory<String, CashBackIsAvailableMessage> producerFactory() {
            Map<String, Object> configProps = new HashMap<>();
            configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
            configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
            configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.springframework.kafka.support.serializer.JsonSerializer");
            // more standard configuration
            return new DefaultKafkaProducerFactory<>(configProps);
        }

        @Bean
        public Map<String, Object> consumerConfigs() {
            Map<String, Object> props = new HashMap<>();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            props.put(ConsumerConfig.GROUP_ID_CONFIG, "jackpot");
            // more standard configuration
            return props;
        }
    }
}

