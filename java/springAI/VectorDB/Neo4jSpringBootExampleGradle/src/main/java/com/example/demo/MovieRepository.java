package com.example.demo;

import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface MovieRepository extends Neo4jRepository<MovieEntity, String> {

  @Query("MATCH (movie:Movie) WHERE movie.title" + " " + "=$title RETURN movie LIMIT 1")
  MovieEntity findOneByTitle(String title);

  List<MovieEntity> findMoreByTitle(String title);

   List<MovieEntity> findByDirectorsName(String directorName);

    List<MovieEntity> findByActedIns(String actorName);

    @Query("""
    MATCH (m:Movie)<-[r:ACTED_IN]-(p:Person)
    WHERE toLower(p.name) CONTAINS toLower($name)
    RETURN m
    """)
    List<MovieEntity> findMoviesByActorName(@Param("name") String name);

    @Query("""
    MATCH (m:Movie)-[a:ACTED_IN]-(p:Person) 
    WHERE toLower(p.name) CONTAINS toLower($name)
    RETURN m, collect(a) as actedIn
    """)
    List<MovieEntity> findMoviesWithFilteredRoles(@Param("name") String name);

    @Query("""
        MATCH (m:Movie)-[a:ACTED_IN]-(p:Person) 
        WHERE toLower(p.name) CONTAINS toLower($name)
        RETURN m.title AS movieTitle, [role IN a.roles | role] AS roles
        """)
    List<MovieRoleProjection> findRolesByActor(@Param("name") String name);

}
