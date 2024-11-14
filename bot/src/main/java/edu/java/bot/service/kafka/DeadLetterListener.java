package edu.java.bot.service.kafka;

import edu.java.bot.controller.dto.LinkUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeadLetterListener {
    @KafkaListener(topics = "${app.deadTopic.name}", groupId = "${spring.kafka.consumer.groupId}")
    public void listenBadUpdates(LinkUpdateRequest update) {
        log.info(update.toString());
    }
}
