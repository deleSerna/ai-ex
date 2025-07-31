package com.polika.dishsuggester;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

//@Component
public class IngestionPipelineVertex {

    private final VectorStore vectorStore;

    @Value("classpath:breakfast/backend.txt")
    Resource backendJobDescription;
    @Value("classpath:jobs/fullstack.txt")
    Resource fullstackJobDescription;
    @Value("classpath:jobs/marketing.txt")
    Resource marketingJobDescription;

    final Path resourceDirectoryPath ;
    int idCounter;
    IngestionPipelineVertex(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        try {
            resourceDirectoryPath = Paths.get(getClass().getClassLoader().getResource("breakfast").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void run() {
        System.out.println("####I  am running");
        List<Document> documents;
        try {
            documents = Files.list(resourceDirectoryPath)
                    .map(x-> new FileSystemResource(x))
                    .map(x->getDocument(x))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("##Adding "+ documents.size() + " to the vector store.");
        /*var backendDocument = getDocument(backendJobDescription, "1");
        var frontendDocument = getDocument(fullstackJobDescription, "2");
        var marketingDocument = getDocument(marketingJobDescription, "3");
        List<Document> documents = List.of(backendDocument, frontendDocument, marketingDocument);*/
        vectorStore.add(new TokenTextSplitter().apply(documents));
    }

    private Document getDocument(Resource textFile) {
        var textReader = new TextReader(textFile);
        textReader.getCustomMetadata().put("jobId", ++idCounter);
        textReader.setCharset(Charset.defaultCharset());
        List<Document> documents = textReader.get();
        if (documents.size() != 1) {
            throw new RuntimeException("Unexpected number of documents=" + documents.size());
        }
        return documents.getFirst();
    }

    public String getvectoreStore() {
        return  vectorStore.getName();
    }
}

