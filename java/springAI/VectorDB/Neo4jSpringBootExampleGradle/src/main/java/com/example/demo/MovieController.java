package com.example.demo;

import java.util.Collections;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
public class MovieController {
  private final MovieRepository movieRepository;

  public MovieController(MovieRepository movieRepository) {
    this.movieRepository = movieRepository;
  }

  @GetMapping("/by-title")
  MovieEntity byTitle(@RequestParam String title) {
    System.out.println("###Request received for byTitle");
    MovieEntity mEn = movieRepository.findOneByTitle(title);
    return mEn == null ? new MovieEntity("NA", "No movie with the given title = " + title) : mEn;
  }

  @GetMapping("/more-by-title")
  List<MovieEntity> moreByTitle(@RequestParam String title) {
      System.out.println("###Request received for moreByTitle");
    List<MovieEntity> mEn = movieRepository.findMoreByTitle(title);
    return mEn == null ? Collections.emptyList() : mEn;
  }

  @GetMapping("/by-director")
  List<MovieEntity> byDirector(@RequestParam String name) {
    System.out.println("###Request received for byDirector");
    List<MovieEntity> mEn = movieRepository.findByDirectorsName(name);
    return mEn == null ? Collections.emptyList() : mEn;
  }

  @GetMapping("/movies-by-name")
  List<MovieEntity> getMoviesByActorName(@RequestParam String name) {
    System.out.println("###Request received for getMoviesByActorName");
    return movieRepository.findMoviesWithFilteredRoles(name);
  }

  @GetMapping("/rolesnmovies-by-name")
  List<MovieRoleProjection> getMoviesAndRolesByActorName(@RequestParam String name) {
    System.out.println("###Request received for getMoveisandRolesByActorName");
    return movieRepository.findRolesByActor(name);
  }
}
