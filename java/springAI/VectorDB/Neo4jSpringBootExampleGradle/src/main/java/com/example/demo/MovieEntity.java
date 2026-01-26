package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("Movie")
public class MovieEntity {

  @Id private final String title;

  @Property("tagline")
  private final String description;

  @Relationship(type = "ACTED_IN", direction = Relationship.Direction.INCOMING)
  private List<Acted_In> actedIns = new ArrayList<>();

  @Relationship(type = "DIRECTED", direction = Relationship.Direction.INCOMING)
  private List<PersonEntity> directors = new ArrayList<>();

  public MovieEntity(String title, String description) {
    this.title = title;
    this.description = description;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public List<Acted_In> getActedIns() {
    return actedIns;
  }

  public void setActedIns(List<Acted_In> actedIns) {
    this.actedIns = actedIns;
  }

  public List<PersonEntity> getDirectors() {
    return directors;
  }

  public void setDirectors(List<PersonEntity> directors) {
    this.directors = directors;
  }
}
