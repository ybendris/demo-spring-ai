package dev.ybendris.demo_spring_ai.translation.controller.impl;

import dev.ybendris.demo_spring_ai.translation.controller.VectorStoreController;
import dev.ybendris.demo_spring_ai.translation.dto.request.VectorStoreUpdateRequest;
import dev.ybendris.demo_spring_ai.translation.service.VectorStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class VectorStoreControllerImpl implements VectorStoreController {
    private final VectorStoreService vectorStoreService;

    @Override
    public ResponseEntity<Void> store(VectorStoreUpdateRequest request) {
        vectorStoreService.addToVectorStore(request);
        return ResponseEntity.ok().build();
    }
}
