package edu.java.service.kafka;

import edu.java.client.dto.LinkUpdateRequest;
import edu.java.configuration.ApplicationConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapperQueueProducer {
    private final ApplicationConfig applicationConfig;
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;

    public void send(LinkUpdateRequest update) {
        kafkaTemplate.send(applicationConfig.topic().name(), update);
    }
}
