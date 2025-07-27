package dev.ybendris.demo_spring_ai.translation.advisor;

import dev.ybendris.demo_spring_ai.translation.exception.TranslationException;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.messages.AbstractMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

public class ForbiddenWordsAdvisor implements CallAdvisor {

    private final List<String> forbiddenWords;

    public ForbiddenWordsAdvisor(List<String> forbiddenWords) {
        this.forbiddenWords = forbiddenWords;
    }

    @Override
    public String getName() {
        return "ForbiddenWordsAdvisor";
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        ChatClientResponse response = callAdvisorChain.nextCall(chatClientRequest);

        Optional.of(response)
                .map(ChatClientResponse::chatResponse)
                .map(ChatResponse::getResult)
                .map(Generation::getOutput)
                .map(AbstractMessage::getText)
                .filter(text -> !CollectionUtils.isEmpty(forbiddenWords))
                .flatMap(text ->
                        forbiddenWords.stream()
                                .filter(text::contains)
                                .findFirst()
                )
                .ifPresent(word -> {
                    throw new TranslationException("Mot interdit détecté : " + word);
                });

        return response;
    }
}
