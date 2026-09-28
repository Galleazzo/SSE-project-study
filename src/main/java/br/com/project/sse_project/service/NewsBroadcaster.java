package br.com.project.sse_project.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class NewsBroadcaster {

    private final List<SseEmitter> subscribers = new CopyOnWriteArrayList<>();

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(0L);
        subscribers.add(emitter);

        emitter.onCompletion(() -> subscribers.remove(emitter));
        emitter.onTimeout(() -> subscribers.remove(emitter));
        emitter.onError(error -> subscribers.remove(emitter));

        return emitter;
    }

    public void publish(String message) {
        for (SseEmitter subscriber : subscribers) {
            try {
                subscriber.send(SseEmitter.event().name("NEWS").data(message));
            } catch (IOException | IllegalStateException exception) {
                subscribers.remove(subscriber);
                subscriber.completeWithError(exception);
            }
        }
    }
}
