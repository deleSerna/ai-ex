package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Neo4jContainer;

@DataNeo4jTest
class MovieRepositoryTCTest {

  private static Neo4jContainer<?> neo4jContainer;

  @BeforeAll
  static void initializeNeo4j() {

    neo4jContainer = new Neo4jContainer<>().withAdminPassword("somePassword");
    neo4jContainer.start();
  }

  @AfterAll
  static void stopNeo4j() {

    neo4jContainer.close();
  }

  @DynamicPropertySource
  static void neo4jProperties(DynamicPropertyRegistry registry) {

    registry.add("spring.neo4j.uri", neo4jContainer::getBoltUrl);
    registry.add("spring.neo4j.authentication.username", () -> "neo4j");
    registry.add("spring.neo4j.authentication.password", neo4jContainer::getAdminPassword);
  }

  @Test
  public void findSomethingShouldWork(@Autowired Neo4jClient client) {

    Optional<Long> result = client.query("MATCH (n) RETURN COUNT(n)").fetchAs(Long.class).one();
    assertThat(result).hasValue(0L);
  }
}
