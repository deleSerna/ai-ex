package com.polika.ai.neo4jgraphdbrag.nodes;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("Person")
public class Person {
  @Id private final String name;

  @Property("born")
  private final int born;

  public Person(String name, int born) {
    this.name = name;
    this.born = born;
  }

  public String getName() {
    return name;
  }

  public int getBorn() {
    return born;
  }
}
