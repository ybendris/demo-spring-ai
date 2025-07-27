package dev.ybendris.demo_spring_ai.translation.service;

import dev.ybendris.demo_spring_ai.translation.dto.response.ProductTranslationResponse;
import dev.ybendris.demo_spring_ai.translation.model.ProductTranslation;

import java.util.Locale;
import java.util.Optional;

public interface TranslationService {
    Optional<ProductTranslationResponse> translate(ProductTranslation productTranslation, Locale locale);
}
