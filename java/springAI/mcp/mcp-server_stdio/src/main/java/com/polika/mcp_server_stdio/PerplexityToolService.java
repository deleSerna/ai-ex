package com.polika.mcp_server_stdio;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerplexityToolService {
    private final ChatModel openAiChatModel;

    @Autowired
    public PerplexityToolService(ChatModel openAiChatModel) {
        this.openAiChatModel = openAiChatModel;
    }

    @Tool(name = "perplexityContextlessSearch", description = "Search using perplexity without RAG")
    public String perplexityContextlessSearch(String input) {
        return this.openAiChatModel.call(input);
    }
}
