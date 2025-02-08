package fr.it.exalt.consumer;

import lombok.Getter;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
@Getter
public class KafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    private CountDownLatch latch = new CountDownLatch(1);
    private String payload;

    @KafkaListener(topics = "jackpot")
    public void receive(ConsumerRecord<?, ?> consumerRecord) {
        LOGGER.info("received payload='{}'", consumerRecord.toString());
        payload = consumerRecord.toString();
        latch.countDown();
    }

}
