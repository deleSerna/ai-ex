package com.polika.ai.neo4jgraphdbrag.repositories;

import com.polika.ai.neo4jgraphdbrag.nodes.Person;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends Neo4jRepository<Person, String> {
  @Query(
      """
          MATCH (m:Movie)<-[r:ACTED_IN]-(p:Person)
          WHERE toLower(m.title) CONTAINS toLower($title)
          RETURN p
          """)
  List<Person> findActorsByMovieTitle(@Param("title") String title);

  //  @Query(
  //      """
  //    MATCH (m:Movie)-[:ACTED_IN]-(p:Person)
  //    WHERE toLower(m.title) CONTAINS toLower($title)
  //    RETURN m.title,
  //           collect(p.name) as actorNames
  //    """)
  //  List<MovieWithActorsProjection> findMoviesWithActors(@Param("title") String title);
}
