package com.polika.mcp_server_stdio;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.List;

@SpringBootApplication
public class McpServerStdioApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerStdioApplication.class, args);
    }

    @Bean
    public List<ToolCallback> tools(EchoToolService echoToolService, PerplexityToolService perplexityToolService) {
        return List.of(ToolCallbacks.from(echoToolService, perplexityToolService));
    }

    @Bean
    @Primary
    public OpenAiChatModel openAiChatModel() {
        OpenAiApi openAiApi = OpenAiApi.builder()
                .apiKey(System.getenv("PERPLEXITY_API_KEY"))
                // TODO - Improve by injecting the properties via value rather than redefining here.
                .baseUrl("https://api.perplexity.ai")
                .completionsPath("/chat/completions")
                .build();
        return OpenAiChatModel
                .builder()
                .defaultOptions(
                        OpenAiChatOptions.builder().model("sonar-pro").build())
                .openAiApi(openAiApi)
                .build();
    }
}
