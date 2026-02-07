package com.polika.ai.neo4jgraphdbrag.services;

import com.polika.ai.neo4jgraphdbrag.nodes.Movie;
import com.polika.ai.neo4jgraphdbrag.repositories.MovieRepository;
import com.polika.ai.neo4jgraphdbrag.repositories.PersonRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.neo4j.driver.Driver;
import org.neo4j.driver.summary.ResultSummary;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.neo4j.Neo4jVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.stereotype.Service;

@Service
public class MovieVectorDBService {
  private final Neo4jVectorStore store;
  private final Neo4jTemplate neo4jTemplate;
  private final Neo4jClient neo4jClient;

  private final MovieRepository movieRepository;
  private final PersonRepository personRepository;

  @Autowired
  public MovieVectorDBService(
      @Qualifier("neo4jMovieTagline") Neo4jVectorStore store,
      Neo4jTemplate neo4jTemplate,
      Driver ne04jDriver,
      MovieRepository movieRepository,
      PersonRepository personRepository) {
    this.store = store;
    this.neo4jTemplate = neo4jTemplate;
    this.neo4jClient = Neo4jClient.create(ne04jDriver);
    this.movieRepository = movieRepository;
    this.personRepository = personRepository;
  }

  public int createVectorForMovies() {
    List<Movie> movies = neo4jTemplate.findAll("MATCH (m:Movie)  RETURN m", Movie.class);
    List<Document> documents =
        movies.stream()
            .filter(movie -> movie.getTagLine() != null)
            .map(
                movie ->
                    new Document(
                        movie.getTagLine(),
                        // Also add the metadata, incase we want to use it for filtering.
                        Map.of("title", movie.getTitle())))
            .collect(Collectors.toList());
    store.add(documents);
    for (Document document : documents) {
      neo4jClient
          .query(
              """
                          MATCH (m:Movie) WHERE m.title = $movieId
                          MATCH (d:MovieEmbedding {id: $docId})
                          CREATE (m)-[:HAS_EMBEDDING]->(d)
                     """)
          .bind(document.getMetadata().get("title"))
          .to("movieId")
          .bind(document.getId())
          .to("docId")
          .run();
    }
    return documents.size();
  }

  /**
   * Delete the embeddings from the NE04j db and from the vector store.
   *
   * @return The number of rows affected
   */
  public int deleteVectorOfMovies() {
    ResultSummary summary =
        neo4jClient
            .query("MATCH (d:MovieEmbedding) DETACH DELETE d") //  Delete nodes + all relationships
            .run();
    return summary.counters().nodesDeleted();
  }

  /** Return matching movies. */
  public String searchForMovies(String query) {
    List<String> movies = getMatchingMovieTitle(query);
    return String.join(",", movies);
  }

  /** Return movies and it's actors in a json form. */
  public String searchForActorsInaMovie(String query) {
    List<String> movies = getMatchingMovieTitle(query);
    String actors =
        movies.stream()
            .map(movieRepository::findActorsWithMovies)
            .flatMap(p -> p.stream())
            .map(p -> p.toString())
            .collect(Collectors.joining(";"));
    return actors;
  }

  private List<String> getMatchingMovieTitle(String query) {
    SearchRequest request =
        new SearchRequest.Builder().query(query).topK(5).similarityThreshold(0.7).build();

    List<String> movies =
        store.similaritySearch(request).stream()
            .limit(5)
            .map(d -> d.getMetadata().get("title").toString())
            .collect(Collectors.toList());
    return movies;
  }
}
