package dev.ybendris.demo_spring_ai.translation.dto.request;

import java.util.Locale;

public record ProductTranslationRequest(String title, String description, Locale locale) {
}
