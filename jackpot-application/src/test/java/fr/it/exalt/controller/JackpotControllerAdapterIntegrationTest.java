package fr.it.exalt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import fr.it.exalt.consumer.KafkaConsumer;
import fr.it.exalt.controller.dto.requests.JackpotCreationRequest;
import fr.it.exalt.controller.dto.requests.JackpotDepositRequest;
import fr.it.exalt.infrastructure.entity.JackpotEntity;
import fr.it.exalt.infrastructure.repository.JackpotRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class JackpotControllerAdapterIntegrationTest {

    @Autowired
    JackpotRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private KafkaConsumer consumer;

    private static ObjectWriter ow;
    public static final String ALREADY_EXISTING_EMAIL = "bar@foo.com";
    public static final String EMAIL = "foo@bar.com";

    @BeforeAll
    static void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        ow = mapper.writer().withDefaultPrettyPrinter();
    }

    @BeforeEach
    void cleanUp() {
        repository.deleteAll();
    }

    @Test
    void should_create_jackpot() throws Exception {
        //given
        BigDecimal amount = BigDecimal.ONE;
        JackpotCreationRequest request = new JackpotCreationRequest(amount, EMAIL);
        String requestJson = ow.writeValueAsString(request);
        this.mockMvc.perform(post("/jackpot").content(requestJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        //TODO vérifier qu'en base on bien les bonnes valeurs pour la cagnotte
    }

    @Test
    void should_deposit_cash_back_on_jackpot() throws Exception {
        //given
        UUID customerId = UUID.randomUUID();
        JackpotEntity jackpotEntity =
                JackpotEntity.builder().customerId(customerId).email(ALREADY_EXISTING_EMAIL).checkoutNumber(1).amount(BigDecimal.TEN).build();
        repository.save(jackpotEntity);
        BigDecimal amount = BigDecimal.ONE;
        JackpotDepositRequest request = new JackpotDepositRequest(customerId, amount);
        String requestJson = ow.writeValueAsString(request);
        //when
        //then
        this.mockMvc.perform(patch("/jackpot").content(requestJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        //TODO vérifier qu'en base on bien les bonnes valeurs pour la cagnotte
    }

    @Test
    void should_notify_cash_back_is_available_on_jackpot() throws Exception {
        //given
        UUID customerId = UUID.randomUUID();
        JackpotEntity jackpotEntity =
                JackpotEntity.builder().customerId(customerId).email(ALREADY_EXISTING_EMAIL).checkoutNumber(4).amount(BigDecimal.TEN).build();
        repository.save(jackpotEntity);
        BigDecimal amount = BigDecimal.ONE;
        JackpotDepositRequest request = new JackpotDepositRequest(customerId, amount);
        String requestJson = ow.writeValueAsString(request);
        //when
        this.mockMvc.perform(patch("/jackpot").content(requestJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        //then
        boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
        assertThat(messageConsumed).isTrue();
        assertThat(consumer.getPayload()).contains(ALREADY_EXISTING_EMAIL);
    }

}
