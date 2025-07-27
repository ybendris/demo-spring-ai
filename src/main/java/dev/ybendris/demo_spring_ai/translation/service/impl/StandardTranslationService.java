package dev.ybendris.demo_spring_ai.translation.service.impl;

import dev.ybendris.demo_spring_ai.translation.bo.Translation;
import dev.ybendris.demo_spring_ai.translation.dto.response.ProductTranslationResponse;
import dev.ybendris.demo_spring_ai.translation.model.ProductTranslation;
import dev.ybendris.demo_spring_ai.translation.repository.TranslationRepository;
import dev.ybendris.demo_spring_ai.translation.service.TranslationService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatOptions;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service("standardTranslationService")
public class StandardTranslationService implements TranslationService {
    private final ChatClient chatClient;
    private final TranslationRepository translationRepository;

    public static final String SYSTEM_PROMPT = """
            You are a professional product translator specializing in marketing content.
            
            While translating:
            Preserve the original tone and style; translate naturally without literal word-for-word translation.
            Keep brand names and product references unchanged.
            
            Respond ONLY with a JSON object containing two fields: "title" and "description".
            
            Translate the following product title and description into {language}.
            """;

    public static final String EMBEDDING_CONTENT = """
            Title: {title}
            Description: {description}
            """;

    public StandardTranslationService(ChatClient.Builder builder, TranslationRepository translationRepository) {
        this.chatClient = builder
                .defaultOptions(VertexAiGeminiChatOptions.builder().build())
                .build();
        this.translationRepository = translationRepository;
    }

    @Override
    public Optional<ProductTranslationResponse> translate(ProductTranslation productTranslation, Locale locale) {
        var translationResponseOptional = Optional.ofNullable(
                chatClient.prompt()
                        .system(promptSystemSpec -> promptSystemSpec.text(SYSTEM_PROMPT)
                                .param("language", locale.getDisplayName(Locale.ENGLISH)))
                        .user(promptUserSpec -> promptUserSpec.text(EMBEDDING_CONTENT)
                                .param("title", productTranslation.title())
                                .param("description", productTranslation.description()))
                        .call()
                        .entity(ProductTranslationResponse.class)
        );

        translationResponseOptional.ifPresent(productTranslationResponse ->
                this.translationRepository.save(new Translation(productTranslation, productTranslationResponse, locale, false)));

        return translationResponseOptional;
    }
}
