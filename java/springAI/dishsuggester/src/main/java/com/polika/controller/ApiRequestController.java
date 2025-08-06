package com.polika.controller;

import com.polika.ingestion.IngestionPipeline;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/doggybag")
public class ApiRequestController {
    private final IngestionPipeline ingestionPipeline;
    private final OpenAiChatModel openAiChatModel;
    private final OllamaEmbeddingModel ollamaEmbeddingModel;
    private final VectorStore ollamaVectorStore;

    @Autowired
    public ApiRequestController(IngestionPipeline ingestionPipeline, OpenAiChatModel openAiChatModel, OllamaEmbeddingModel ollamaEmbeddingModel,
                                VectorStore ollamaVectorStore) {
        this.ingestionPipeline = ingestionPipeline;
        this.openAiChatModel = openAiChatModel;
        this.ollamaEmbeddingModel = ollamaEmbeddingModel;
        this.ollamaVectorStore = ollamaVectorStore;
    }

    @GetMapping("/ingest")
    public boolean ingest() {
        System.out.println("###Request reached to Ingest.");
        return ingestionPipeline.ingest();
    }

    @GetMapping("/delete")
    public boolean delete() {
        System.out.println("###Request reached to delete.");
        return ingestionPipeline.delete();
    }

    @GetMapping("/chat")
    public Map<String, String> generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        System.out.println("###User incoming question." + message);
        List<Document> docs = ollamaVectorStore.similaritySearch(message);
        String context = docs.stream().map(Document::getText).collect(Collectors.joining("\n"));
        String prompt = "Context:\n" + context + "\n\nQuestion: " + message;
        System.out.println(prompt);
        return Map.of("output", this.openAiChatModel.call(prompt));
    }
}
