package edu.java.bot.kafka;

import edu.java.bot.BotApplication;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.controller.dto.LinkUpdateRequest;
import edu.java.bot.service.LinkUpdateHandler;
import edu.java.bot.service.kafka.DeadLetterListener;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.annotation.DirtiesContext;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.verify;

@DirtiesContext
@SpringBootTest(classes = {BotApplication.class})
@EnableConfigurationProperties(ApplicationConfig.class)
public class KafkaUpdateListenerTest extends KafkaIntegrationTest {
    private static final LinkUpdateRequest REQUEST = new LinkUpdateRequest();

    @Autowired
    private ApplicationConfig applicationConfig;

    @Value("${spring.kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @MockBean
    private LinkUpdateHandler linkUpdateHandlerMock;

    @SpyBean
    private DeadLetterListener deadLetterListener;

    @Test
    @DisplayName("valid update message")
    void validUpdateMessage() {
        try (KafkaProducer<String, LinkUpdateRequest> kafkaProducer = new KafkaProducer<>(getConfigs())) {
            kafkaProducer.send(new ProducerRecord<>(applicationConfig.topic().name(), REQUEST));

            verify(linkUpdateHandlerMock, after(1000).times(1)).handle(REQUEST);
        }
    }

    @Test
    void listenInvalidUpdateMessage() {
        Mockito.doThrow(RuntimeException.class)
            .when(linkUpdateHandlerMock)
            .handle(any());

        try (KafkaProducer<String, LinkUpdateRequest> kafkaProducer = new KafkaProducer<>(getConfigs())) {
            kafkaProducer.send(new ProducerRecord<>(applicationConfig.topic().name(), REQUEST));

            verify(linkUpdateHandlerMock, after(1000).times(1)).handle(REQUEST);

            verify(deadLetterListener, after(1000).times(1)).listenBadUpdates(any());
        }
    }

    private Map<String, Object> getConfigs() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return configs;
    }
}
