package dev.ybendris.demo_spring_ai.chat.controller;

import dev.ybendris.demo_spring_ai.chat.dto.request.MessageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

@RequestMapping("/chat")
public interface ChatController {

    @PostMapping("")
    ResponseEntity<String> chat(@RequestBody MessageRequest request);

    @PostMapping(path = "/streaming", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<String> chatStreaming(@RequestBody MessageRequest request);
}
