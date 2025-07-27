package dev.ybendris.demo_spring_ai.translation.bo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "prompt")
public class Prompt {
    @Id
    private Long id;
    private String text;
}
