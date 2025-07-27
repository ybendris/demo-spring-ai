package dev.ybendris.demo_spring_ai.translation.advisor;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.stream.Collectors;

public class LexicalPreferenceAdvisor implements CallAdvisor {
    private final Map<String, String> lexicalPreferences;
    private static final String PREFERENCE_TEMPLATE = "Use '%s' instead of '%s'.";

    public LexicalPreferenceAdvisor(Map<String, String> lexicalPreferences) {
        this.lexicalPreferences = lexicalPreferences;
    }

    @Override
    public String getName() {
        return "LexicalPreferenceAdvisor";
    }

    @Override
    public int getOrder() {
        return -100;
    }


    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain callAdvisorChain) {
        if (CollectionUtils.isEmpty(this.lexicalPreferences)) {
            return callAdvisorChain.nextCall(request);
        }

        Prompt augmentedPrompt = request.prompt().augmentSystemMessage(
                lexicalPreferences.entrySet().stream()
                        .map(entry -> PREFERENCE_TEMPLATE.formatted(entry.getValue(), entry.getKey()))
                        .collect(Collectors.joining("\r\n"))
        );

        ChatClientRequest newRequest = ChatClientRequest.builder()
                .prompt(augmentedPrompt)
                .context(Map.copyOf(request.context()))
                .build();

        return callAdvisorChain.nextCall(newRequest);
    }
}
