package br.com.project.sse_project.controller;

import br.com.project.sse_project.service.NewsBroadcaster;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class SSEController {

    private final NewsBroadcaster newsBroadcaster;

    public SSEController(NewsBroadcaster newsBroadcaster) {
        this.newsBroadcaster = newsBroadcaster;
    }

    @GetMapping("/news")
    public SseEmitter subscribeToNews() {
        return newsBroadcaster.subscribe();
    }
}
