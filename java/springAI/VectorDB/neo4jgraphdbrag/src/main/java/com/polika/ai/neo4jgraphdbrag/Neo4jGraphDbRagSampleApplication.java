package com.polika.ai.neo4jgraphdbrag;

import org.neo4j.driver.Driver;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.neo4j.Neo4jVectorStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Neo4jGraphDbRagSampleApplication {

  public static void main(String[] args) {
    SpringApplication.run(Neo4jGraphDbRagSampleApplication.class, args);
  }

  @Bean("neo4jMovieTagline")
  public Neo4jVectorStore vectorStore(Driver driver, EmbeddingModel embeddingModel) {
    return Neo4jVectorStore.builder(driver, embeddingModel)
        .databaseName("neo4j")
        .indexName("movie_embeddings")
        .distanceType(Neo4jVectorStore.Neo4jDistanceType.COSINE)
        .label("MovieEmbedding")
        .embeddingProperty("embedding")
        .initializeSchema(true)
        .build();
  }
}
