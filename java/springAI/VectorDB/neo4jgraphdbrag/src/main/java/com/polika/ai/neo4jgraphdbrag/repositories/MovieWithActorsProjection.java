package com.polika.ai.neo4jgraphdbrag.repositories;

import java.util.List;

public class MovieWithActorsProjection {
  public String title;

  public List<String> actorNames; // flattened actors

  @Override
  public String toString() {
    return "{" + "title='" + title + '\'' + ", actorNames=" + actorNames + '}';
  }
}
