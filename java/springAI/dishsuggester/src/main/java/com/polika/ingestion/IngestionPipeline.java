package com.polika.ingestion;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class IngestionPipeline {
    final Path resourceDirectoryPath;
    private final String DEFAULT_VECTOR_TABLE_NAME = "vector_store";
    private final VectorStore vectorStore;
    int idCounter;
    List<String> ids = new ArrayList<>();
    @Autowired
    private JdbcTemplate jdbcTemplate;

    IngestionPipeline(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        try {
            resourceDirectoryPath = Paths.get(getClass().getClassLoader().getResource("breakfast").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Use this if we want to ingest automatically on start up.
     */
    /*@PostConstruct
    public void run() {
        List<Document> documents;
        try {
            documents = Files.list(resourceDirectoryPath)
                    .map(x -> new FileSystemResource(x))
                    .map(x -> getDocument(x))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("##Adding " + documents.size() + " to the vector store.");
        vectorStore.add(new TokenTextSplitter().apply(documents));
    }
    */
    public boolean ingest() {
        List<Document> documents;
        try {
            documents = Files.list(resourceDirectoryPath)
                    .map(x -> new FileSystemResource(x))
                    .map(x -> getDocument(x))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("##Adding " + documents.size() + " to the vector store.");
        vectorStore.add(new TokenTextSplitter().apply(documents));
        return !documents.isEmpty();
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

    public boolean delete() {
        //TODO(): Improve this by using vector store.
        jdbcTemplate.execute("TRUNCATE TABLE " + System.getenv("YOUR_DOGGY_DB") + "." + DEFAULT_VECTOR_TABLE_NAME);
        return true;
    }
}

