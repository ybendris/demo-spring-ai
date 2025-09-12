package dev.ybendris.demo_spring_ai.chat.tools;

import org.springframework.ai.tool.annotation.Tool;

public class ExempleTools {

    @Tool(description = "Tell what the current collection is")
    String getCurrentCollections() {
        return "ETE 25";
    }
}
