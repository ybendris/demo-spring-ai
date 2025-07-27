package dev.ybendris.demo_spring_ai.translation.controller;

import dev.ybendris.demo_spring_ai.translation.dto.request.ProductTranslationRequest;
import dev.ybendris.demo_spring_ai.translation.dto.response.ProductTranslationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/translate")
public interface TranslationController {

    @PostMapping()
    ResponseEntity<ProductTranslationResponse> translate(@RequestBody ProductTranslationRequest request);

    @PostMapping("/rag")
    ResponseEntity<ProductTranslationResponse> translateWithRag(@RequestBody ProductTranslationRequest request);
}
