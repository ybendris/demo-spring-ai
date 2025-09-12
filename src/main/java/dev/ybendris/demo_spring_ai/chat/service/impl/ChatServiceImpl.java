package dev.ybendris.demo_spring_ai.chat.service.impl;

import dev.ybendris.demo_spring_ai.chat.service.ChatService;
import dev.ybendris.demo_spring_ai.chat.tools.ExempleTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;

    public ChatServiceImpl(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultOptions(VertexAiGeminiChatOptions.builder().build())
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(MessageWindowChatMemory.builder()
                                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                                .maxMessages(10)
                                .build()
                        ).build())
                .build();
    }

    @Override
    public String chat(String request) {
        return this.chatClient.prompt(request).tools(new ExempleTools()).call().content();
    }

    @Override
    public Flux<String> chatStreaming(String request) {
        return this.chatClient.prompt(request).tools(new ExempleTools()).stream().content();
    }
}
