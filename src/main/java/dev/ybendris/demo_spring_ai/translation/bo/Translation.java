package dev.ybendris.demo_spring_ai.translation.bo;

import dev.ybendris.demo_spring_ai.translation.dto.response.ProductTranslationResponse;
import dev.ybendris.demo_spring_ai.translation.model.ProductTranslation;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Locale;

@Document(collection = "translation")
public class Translation {
    @Id
    private String id;
    private String sourceTitle;
    private String sourceDescription;
    private Locale sourceLocale;
    private String translatedTitle;
    private String translatedDescription;
    private Locale targetLocale;
    private boolean rag;

    public Translation(ProductTranslation productTranslation, ProductTranslationResponse productTranslationResponse, Locale locale, boolean rag) {
        this.sourceTitle = productTranslation.title();
        this.sourceDescription = productTranslation.description();
        this.sourceLocale = Locale.FRANCE;
        this.translatedTitle = productTranslationResponse.title();
        this.translatedDescription = productTranslationResponse.description();
        this.targetLocale = locale;
        this.rag = rag;
    }
}
