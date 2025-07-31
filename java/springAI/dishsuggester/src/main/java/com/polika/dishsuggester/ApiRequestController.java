package com.polika.dishsuggester;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doggybag")
public class ApiRequestController {
    private final IngestionPipeline ingestionPipeline;
    private  OpenAiChatModel chatModel;

    @Autowired
    public ApiRequestController(IngestionPipeline ingestionPipeline) {
        System.out.println("#####In ApiRequestController");
        this.ingestionPipeline = ingestionPipeline;
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
}
