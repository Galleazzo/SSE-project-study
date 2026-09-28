package br.com.project.sse_project.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import br.com.project.sse_project.service.NewsBroadcaster;

@Component
public class KafkaConsumer {

    private final NewsBroadcaster newsBroadcaster;

    public KafkaConsumer(NewsBroadcaster newsBroadcaster) {
        this.newsBroadcaster = newsBroadcaster;
    }

    @KafkaListener(topics = "demo", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) {
        System.out.println("Received message: " + message);
        newsBroadcaster.publish(message);
    }
}
