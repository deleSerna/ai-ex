package com.polika.chat.service;

import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;

public class WatDoJe {
    private final VertexAiGeminiChatModel chatModel;


    public WatDoJe(VertexAiGeminiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String chat(String message) {
        return this.chatModel.call(message);}
}
