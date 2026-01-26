package com.example.demo;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface PersonRepository extends Neo4jRepository<PersonEntity, String> {

  @Query("MATCH (p:Person) WHERE p.name" + " " + "=$name RETURN p")
  List<PersonEntity> findByName(String name);
}
