package com.polika.ai.neo4jgraphdbrag.controller;

import com.polika.ai.neo4jgraphdbrag.services.MovieEmbeddingService;
import com.polika.ai.neo4jgraphdbrag.services.MovieVectorDBService;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/neo4j")
public class GraphRagController {
  private final MovieEmbeddingService movieEmbeddingService;
  private final MovieVectorDBService movieVectorDbService;

  @Autowired
  public GraphRagController(
      MovieEmbeddingService movieEmbeddingService, MovieVectorDBService movieVectorDbService) {
    this.movieEmbeddingService = movieEmbeddingService;
    this.movieVectorDbService = movieVectorDbService;
  }

  /**
   * Add embedding for movies which does not have it yet.
   *
   * @return Percentage of movies where a new embedding is added for it.
   */
  @GetMapping("/embed/movie/create")
  public float createVectorEmbeddingMovie() {
    System.out.println("###Request reached to create vector embedding for movie node");
    return movieEmbeddingService.embedExistingMovies();
  }

  /**
   * Delete embedding from movies nodes
   *
   * @return Number of movies affected.
   */
  @GetMapping("/embed/movie/delete")
  public float deleteVectorEmbeddingMovie() {
    System.out.println("###Request reached to deleteVectorEmbeddingMovie for movie node");
    return movieEmbeddingService.deleteEmbeddingFromMovies();
  }

  @GetMapping("/movie/actors")
  public List<String> getActorsWhoActedMoviesWithGivenTagline(String tagline) {
    System.out.println("###Request reached to getActorsWhoActedMoviesWithGivenTagline");
    return Collections.emptyList(); // movieEmbeddingService.embedExistingMovies();
  }

  @GetMapping("/movie/vector/create")
  public int createVector() {
    System.out.println("###Request reached to createVector");
    return movieVectorDbService.createVectorForMovies();
  }

  @GetMapping("/movie/vector/delete")
  public int deleteVector() {
    System.out.println("###Request reached to createVector");
    return movieVectorDbService.deleteVectorOfMovies();
  }

  @GetMapping("/movie/tagline")
  public String getMovie(String query) {
    System.out.println("###Request reached to getMovie");
    return movieVectorDbService.searchForMovies(query);
  }

  @GetMapping("/actors/tagline")
  public String getActorsOfAMovie(String query) {
    System.out.println("###Request reached to getActorsOfAMovie");
    return movieVectorDbService.searchForActorsInaMovie(query);
  }
}
