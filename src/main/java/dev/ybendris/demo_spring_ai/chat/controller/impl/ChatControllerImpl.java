package dev.ybendris.demo_spring_ai.chat.controller.impl;

import dev.ybendris.demo_spring_ai.chat.controller.ChatController;
import dev.ybendris.demo_spring_ai.chat.dto.request.MessageRequest;
import dev.ybendris.demo_spring_ai.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class ChatControllerImpl implements ChatController {
    private final ChatService chatService;

    @Override
    public ResponseEntity<String> chat(MessageRequest request) {
        return ResponseEntity.ok(chatService.chat(request.message()));
    }

    @Override
    public Flux<String> chatStreaming(MessageRequest request) {
        return chatService.chatStreaming(request.message());
    }
}
