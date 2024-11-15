package edu.java.service.sender;

import edu.java.client.dto.LinkUpdateRequest;
import edu.java.configuration.ApplicationConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.useQueue", havingValue = "true")
public class ScrapperQueueProducer implements UpdateSender {
    private final ApplicationConfig applicationConfig;
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;

    @Override
    public void send(LinkUpdateRequest update) {
        kafkaTemplate.send(applicationConfig.topic().name(), update);
    }
}
