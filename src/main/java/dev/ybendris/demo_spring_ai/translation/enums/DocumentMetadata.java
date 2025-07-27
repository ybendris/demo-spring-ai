package dev.ybendris.demo_spring_ai.translation.enums;

import lombok.Getter;

@Getter
public enum DocumentMetadata {
    SOURCE_LANGUAGE("sourceLanguage"),
    SOURCE_TITLE("sourceTitle"),
    SOURCE_DESCRIPTION("sourceDescription"),
    TARGET_LANGUAGE("targetLanguage"),
    TARGET_TITLE("targetTitle"),
    TARGET_DESCRIPTION("targetDescription");

    private final String key;

    DocumentMetadata(String key) {
        this.key = key;
    }
}
