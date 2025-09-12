package dev.ybendris.demo_spring_ai.chat.service;


import reactor.core.publisher.Flux;

public interface ChatService {
    String chat(String request);

    Flux<String> chatStreaming(String request);
}
