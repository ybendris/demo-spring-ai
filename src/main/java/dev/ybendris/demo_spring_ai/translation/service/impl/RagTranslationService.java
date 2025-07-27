package dev.ybendris.demo_spring_ai.translation.service.impl;

import dev.ybendris.demo_spring_ai.translation.bo.Translation;
import dev.ybendris.demo_spring_ai.translation.dto.response.ProductTranslationResponse;
import dev.ybendris.demo_spring_ai.translation.enums.DocumentMetadata;
import dev.ybendris.demo_spring_ai.translation.model.ProductTranslation;
import dev.ybendris.demo_spring_ai.translation.repository.TranslationRepository;
import dev.ybendris.demo_spring_ai.translation.service.TranslationService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatOptions;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("ragTranslationService")
public class RagTranslationService implements TranslationService {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;
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

    public static final String CONTEXT_PROMPT = """
            Translations hints are provided below.
            
            ---------------------
            {context}
            ---------------------
            
            Follow these rules:
            1. Avoid statements like Based on the hints..." or "The provided information...".
            
            Given the examples and the rules, translate.
            
            Query:
            {query}
            """;

    public RagTranslationService(ChatClient.Builder builder, VectorStore vectorStore, TranslationRepository translationRepository) {
        this.chatClient = builder
                .defaultOptions(VertexAiGeminiChatOptions.builder().build())
                .build();
        this.vectorStore = vectorStore;
        this.translationRepository = translationRepository;
    }

    @Override
    public Optional<ProductTranslationResponse> translate(ProductTranslation productTranslation, Locale locale) {
        var translationResponseOptional = Optional.ofNullable(
                chatClient.prompt()
                        .advisors(getRetrievalAugmentationAdvisor(locale))
                        .system(promptSystemSpec -> promptSystemSpec.text(SYSTEM_PROMPT)
                                .param("language", locale.getDisplayName(Locale.ENGLISH)))
                        .user(promptUserSpec -> promptUserSpec.text(EMBEDDING_CONTENT)
                                .param("title", productTranslation.title())
                                .param("description", productTranslation.description()))
                        .call()
                        .entity(ProductTranslationResponse.class)
        );

        translationResponseOptional.ifPresent(productTranslationResponse ->
                this.translationRepository.save(new Translation(productTranslation, productTranslationResponse, locale, true)));

        return translationResponseOptional;
    }

    private RetrievalAugmentationAdvisor getRetrievalAugmentationAdvisor(Locale locale) {
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(
                        VectorStoreDocumentRetriever.builder()
                                .vectorStore(vectorStore)
                                .similarityThreshold(0.70)
                                .topK(2)
                                .filterExpression(
                                        new FilterExpressionBuilder()
                                                .eq(DocumentMetadata.TARGET_LANGUAGE.getKey(), locale.toLanguageTag())
                                                .build()
                                )
                                .build()
                )
                .queryAugmenter(
                        ContextualQueryAugmenter.builder()
                                .allowEmptyContext(true)
                                .promptTemplate(
                                        new PromptTemplate(CONTEXT_PROMPT)
                                )
                                .documentFormatter(CUSTOM_DOCUMENT_FORMATTER)
                                .build()
                )
                .order(Ordered.HIGHEST_PRECEDENCE)
                .build();
    }

    private static final Function<List<Document>, String> CUSTOM_DOCUMENT_FORMATTER = documents -> {
        AtomicInteger counter = new AtomicInteger(1);
        return documents.stream()
                .map(document -> new StringBuilder()
                        .append("##Exemple %s##".formatted(counter.getAndIncrement())).append(System.lineSeparator())
                        .append("Base Translation:").append(System.lineSeparator())
                        .append(document.getMetadata().get(DocumentMetadata.SOURCE_TITLE.getKey())).append(System.lineSeparator())
                        .append(document.getMetadata().get(DocumentMetadata.SOURCE_DESCRIPTION.getKey())).append(System.lineSeparator())
                        .append(System.lineSeparator())
                        .append("Translated:").append(System.lineSeparator())
                        .append(document.getMetadata().get(DocumentMetadata.TARGET_TITLE.getKey())).append(System.lineSeparator())
                        .append(document.getMetadata().get(DocumentMetadata.TARGET_DESCRIPTION.getKey())).append(System.lineSeparator())
                )
                .collect(Collectors.joining(System.lineSeparator()));
    };
}
