package dev.ybendris.demo_spring_ai.translation.service.impl;

import dev.ybendris.demo_spring_ai.translation.dto.request.VectorStoreUpdateRequest;
import dev.ybendris.demo_spring_ai.translation.enums.DocumentMetadata;
import dev.ybendris.demo_spring_ai.translation.service.VectorStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VectorStoreServiceImpl implements VectorStoreService {

    private final VectorStore vectorStore;

    @Override
    public void addToVectorStore(VectorStoreUpdateRequest request) {
        String content = RagTranslationService.EMBEDDING_CONTENT
                .replace("{title}", request.sourceTitle())
                .replace("{description}", request.sourceDescription());

        var document = new Document(
                content,
                Map.of(
                        DocumentMetadata.SOURCE_LANGUAGE.getKey(), request.sourceLanguage(),
                        DocumentMetadata.SOURCE_TITLE.getKey(), request.sourceTitle(),
                        DocumentMetadata.SOURCE_DESCRIPTION.getKey(), request.sourceDescription(),
                        DocumentMetadata.TARGET_LANGUAGE.getKey(), request.targetLanguage(),
                        DocumentMetadata.TARGET_TITLE.getKey(), request.targetTitle(),
                        DocumentMetadata.TARGET_DESCRIPTION.getKey(), request.targetDescription()
                )
        );

        //Spring ai se charge d'appeler le model d'embedding quand je veux stocker un document dans un vector store
        vectorStore.add(List.of(document));
    }
}
