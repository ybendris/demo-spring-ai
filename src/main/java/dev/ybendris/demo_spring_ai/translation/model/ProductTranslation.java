package dev.ybendris.demo_spring_ai.translation.model;

import dev.ybendris.demo_spring_ai.translation.dto.request.ProductTranslationRequest;

public record ProductTranslation(String title, String description) {
    public ProductTranslation(ProductTranslationRequest request){
        this(request.title(), request.description());
    }
}
