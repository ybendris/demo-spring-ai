package dev.ybendris.demo_spring_ai.translation.controller;

import dev.ybendris.demo_spring_ai.translation.dto.request.VectorStoreUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/store")
public interface VectorStoreController {

    @PostMapping()
    ResponseEntity<Void> store(@RequestBody VectorStoreUpdateRequest request);
}
