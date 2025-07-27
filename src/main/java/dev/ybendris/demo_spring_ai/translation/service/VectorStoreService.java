package dev.ybendris.demo_spring_ai.translation.service;

import dev.ybendris.demo_spring_ai.translation.dto.request.VectorStoreUpdateRequest;

public interface VectorStoreService {
    void addToVectorStore(VectorStoreUpdateRequest request);
}
