package dev.ybendris.demo_spring_ai.translation.service;

import dev.ybendris.demo_spring_ai.translation.advisor.ForbiddenWordsAdvisor;
import dev.ybendris.demo_spring_ai.translation.advisor.LexicalPreferenceAdvisor;
import dev.ybendris.demo_spring_ai.translation.configuration.TranslationProperties;
import dev.ybendris.demo_spring_ai.translation.dto.ProductTranslationRequest;
import dev.ybendris.demo_spring_ai.translation.dto.ProductTranslationResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatOptions;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@EnableConfigurationProperties(TranslationProperties.class)
public class TranslationServiceImpl implements TranslationService {
    private final TranslationProperties properties;
    private final ChatClient chatClient;

    public TranslationServiceImpl(ChatClient.Builder builder, TranslationProperties properties) {
        this.properties = properties;
        this.chatClient = builder
                .defaultSystem("You are a professional product translator specializing in marketing content.")
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultOptions(VertexAiGeminiChatOptions.builder().build())
                .build();
    }

    @Override
    public ProductTranslationResponse translate(ProductTranslationRequest request) {
        String promptTemplate = """
                Translate the following product title and description into {language}.
                Preserve the original tone, style, and intent of the text.
                Adapt the translation to sound natural in the target language, avoiding literal word-for-word translation.
                Keep brand names and product references unchanged.
                Respond ONLY with a JSON object containing two fields: "title" and "description".
                The input product details are:
                Title: {title}
                Description: {description}
                """;

        return chatClient.prompt()
                .advisors(
                        new ForbiddenWordsAdvisor(properties.forbiddenWords().get(request.locale())),
                        new LexicalPreferenceAdvisor(properties.preferences().get(request.locale()))
                )
                .user(u -> u.text(promptTemplate)
                        .param("title", request.title())
                        .param("description", request.description())
                        .param("language", request.locale().getDisplayName(Locale.ENGLISH)))
                .call()
                .entity(ProductTranslationResponse.class);
    }
}
