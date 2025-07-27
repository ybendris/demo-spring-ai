package dev.ybendris.demo_spring_ai.translation.controller;

import dev.ybendris.demo_spring_ai.translation.dto.ProductTranslationRequest;
import dev.ybendris.demo_spring_ai.translation.dto.ProductTranslationResponse;
import dev.ybendris.demo_spring_ai.translation.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestControllerImpl implements TranslationController {
    private final TranslationService translationService;

    @Override
    public ResponseEntity<ProductTranslationResponse> translate(ProductTranslationRequest request) {
        return ResponseEntity.ok(this.translationService.translate(request));
    }
}
