package dev.ybendris.demo_spring_ai.translation.dto.request;

public record VectorStoreUpdateRequest(String sourceLanguage, String sourceTitle, String sourceDescription,
                                       String targetLanguage, String targetTitle, String targetDescription) {
}
