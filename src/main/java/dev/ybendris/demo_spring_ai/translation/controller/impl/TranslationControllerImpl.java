package dev.ybendris.demo_spring_ai.translation.controller.impl;

import dev.ybendris.demo_spring_ai.translation.controller.TranslationController;
import dev.ybendris.demo_spring_ai.translation.dto.request.ProductTranslationRequest;
import dev.ybendris.demo_spring_ai.translation.dto.response.ProductTranslationResponse;
import dev.ybendris.demo_spring_ai.translation.model.ProductTranslation;
import dev.ybendris.demo_spring_ai.translation.service.TranslationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TranslationControllerImpl implements TranslationController {
    private final TranslationService standardTranslationService;
    private final TranslationService ragTranslationService;

    public TranslationControllerImpl(
        @Qualifier("standardTranslationService") TranslationService standardTranslationService,
        @Qualifier("ragTranslationService") TranslationService ragTranslationService) {
        this.standardTranslationService = standardTranslationService;
        this.ragTranslationService = ragTranslationService;
    }

    @Override
    public ResponseEntity<ProductTranslationResponse> translate(ProductTranslationRequest request) {
        return this.standardTranslationService.translate(new ProductTranslation(request), request.locale())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @Override
    public ResponseEntity<ProductTranslationResponse> translateWithRag(ProductTranslationRequest request) {
        return this.ragTranslationService.translate(new ProductTranslation(request), request.locale())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
