package com.example.demo;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Autowired;;
public class ChatService {
    private final VertexAiGeminiChatModel chatModel;

    @Autowired
    public ChatService(VertexAiGeminiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String chat(String message) {
        return this.chatModel.call(message);}
}
