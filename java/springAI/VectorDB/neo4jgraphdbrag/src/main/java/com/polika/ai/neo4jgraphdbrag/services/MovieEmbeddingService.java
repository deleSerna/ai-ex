package com.polika.ai.neo4jgraphdbrag.services;

import com.polika.ai.neo4jgraphdbrag.nodes.Movie;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.neo4j.driver.Driver;
import org.neo4j.driver.summary.ResultSummary;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;

@Service
public class MovieEmbeddingService {

  private final Neo4jTemplate neo4jTemplate;
  private final EmbeddingModel embeddingModel;
  private final Neo4jClient neo4jClient;

  @Autowired
  public MovieEmbeddingService(
      Neo4jTemplate neo4jTemplate,
      EmbeddingModel embeddingModel,
      Driver ne04jDriver) {
    this.neo4jTemplate = neo4jTemplate;
    this.embeddingModel = embeddingModel;
    this.neo4jClient = Neo4jClient.create(ne04jDriver);
  }

  /**
   * Add an embedding for the tagline of the movie in the existing movie node.
   *
   * @return Percentage of movies where a new embedding is added for it.
   */
  public float embedExistingMovies() {
    // Find unembedded nodes.
    AtomicInteger counter = new AtomicInteger();
    List<Movie> movies =
        // We can also use Neo4J client here.
        neo4jTemplate.findAll("MATCH (m:Movie) WHERE m.embedding IS NULL RETURN m", Movie.class);
    movies.forEach(
        movie -> {
          if (movie.getTagLine() != null) {
            float[] embedding = embeddingModel.embed(movie.getTagLine());
            boolean success = addEmbeddingToSpecificMovie(movie.getTitle(), embedding);
            if (success) {
              counter.getAndIncrement();
            }
          }
        });

    return counter.intValue() / (movies.size() + 0.0f);
  }

  /**
   * @param title Title (identifier) of the movie.
   * @param embedding Embedding of the movie
   * @return True, if embedding for the given movies is added.
   */
  public boolean addEmbeddingToSpecificMovie(String title, float[] embedding) {
    ResultSummary summary =
        neo4jClient
            .query(
                """
                        MATCH (m:Movie)
                        WHERE m.title  = $title
                        SET m.embedding = $embedding
                        """)
            .bind(title)
            .to("title")
            .bind(embedding)
            .to("embedding") // ← float[] works directly
            .run();
    return summary.counters().propertiesSet() != 0;
  }

  public int deleteEmbeddingFromMovies() {
    ResultSummary summary =
        neo4jClient
            .query(
                """
                          MATCH (m:Movie)
                          SET m.embedding = NULL
                        """)
            .run();
    return summary.counters().propertiesSet();
  }
}
