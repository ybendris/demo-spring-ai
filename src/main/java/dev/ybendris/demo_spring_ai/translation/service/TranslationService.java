package dev.ybendris.demo_spring_ai.translation.service;

import dev.ybendris.demo_spring_ai.translation.dto.ProductTranslationRequest;
import dev.ybendris.demo_spring_ai.translation.dto.ProductTranslationResponse;

public interface TranslationService {
    ProductTranslationResponse translate(ProductTranslationRequest location);
}
