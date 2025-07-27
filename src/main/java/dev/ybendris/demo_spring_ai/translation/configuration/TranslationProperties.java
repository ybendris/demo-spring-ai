package dev.ybendris.demo_spring_ai.translation.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@ConfigurationProperties(prefix = "app.translation")
public record TranslationProperties(
        Map<Locale, Map<String, String>> preferences,
        Map<Locale, List<String>> forbiddenWords
) {}
