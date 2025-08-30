package az.cybernet.invoice.kafka;

import az.cybernet.invoice.dto.event.InvoiceCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InvoiceEventProducer {

    private final KafkaTemplate<String, InvoiceCreatedEvent> kafkaTemplate;
    private final String topic;

    public InvoiceEventProducer(KafkaTemplate<String, InvoiceCreatedEvent> kafkaTemplate, Environment environment) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = environment.getProperty("spring.kafka.topics.notifications");
    }

    public void sendInvoiceCreatedEvent(InvoiceCreatedEvent event) {
        kafkaTemplate.send(topic, String.valueOf(event.getUserId()), event);
        log.info("InvoiceCreatedEvent sent to topic {}: {}", topic, event);
    }
}
