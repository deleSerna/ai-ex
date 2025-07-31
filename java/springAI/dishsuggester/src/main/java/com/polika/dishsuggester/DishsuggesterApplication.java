package com.polika.dishsuggester;

import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class DishsuggesterApplication {
	/*@Autowired
	private OllamaEmbeddingModel embeddingModel;
	@Autowired
	private JdbcTemplate jdbcTemplate;*/
	public static void main(String[] args) {
		SpringApplication.run(DishsuggesterApplication.class, args);
	}
/*
	@Bean
	public VectorStore vectorStore(OllamaEmbeddingModel embeddingModel) {
		return  PgVectorStore.builder(jdbcTemplate, embeddingModel).build();
	}*/
}
