package edu.java.bot.service.kafka;

import edu.java.bot.controller.dto.LinkUpdateRequest;
import edu.java.bot.service.LinkUpdateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaUpdateListener {
    private final LinkUpdateHandler linkUpdateHandler;
    private final DeadLetterProducer deadLetterProducer;

    @KafkaListener(
        topics = "${app.topic.name}",
        groupId = "${spring.kafka.consumer.groupId}"
    )
    public void listen(LinkUpdateRequest request) {
        try {
            linkUpdateHandler.handle(request);
        } catch (Exception e) {
            deadLetterProducer.sendToDeadLetterQueue(request);
        }
    }
}
