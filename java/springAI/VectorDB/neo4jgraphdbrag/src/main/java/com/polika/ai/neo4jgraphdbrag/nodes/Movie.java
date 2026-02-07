package com.polika.ai.neo4jgraphdbrag.nodes;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.neo4j.core.schema.*;

@Node("Movie")
public class Movie {
  @Id private final String title;

  @Property("tagline")
  private final String tagline;

  @Relationship(type = "ACTED_IN", direction = Relationship.Direction.INCOMING)
  private List<Acted_In> actedIns = new ArrayList<>();

  public Movie(String title, String tagline) {
    this.title = title;
    this.tagline = tagline;
  }

  public String getTitle() {
    return title;
  }

  public String getTagLine() {
    return tagline;
  }

  public List<Acted_In> getActedIns() {
    return actedIns;
  }

  public void setActedIns(List<Acted_In> actedIns) {
    this.actedIns = actedIns;
  }
}
