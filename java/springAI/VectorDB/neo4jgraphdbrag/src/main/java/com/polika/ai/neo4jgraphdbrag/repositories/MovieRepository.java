package com.polika.ai.neo4jgraphdbrag.repositories;

import com.polika.ai.neo4jgraphdbrag.nodes.Movie;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface MovieRepository extends Neo4jRepository<Movie, String> {
  @Query(
      """
    MATCH (m:Movie)<-[r:ACTED_IN]-(p:Person)
    WHERE toLower(p.name) CONTAINS toLower($name)
    RETURN m
    """)
  List<Movie> findMoviesByActorName(@Param("name") String name);

  @Query(
      """
    MATCH (m:Movie)<-[r:ACTED_IN]-(p:Person)
    WHERE toLower(m.title) CONTAINS toLower($title)
    RETURN m.title AS title,
           collect(p.name) as actorNames
    """)
  List<MovieWithActorsProjection> findActorsWithMovies(@Param("title") String title);
}
