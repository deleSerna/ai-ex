package com.polika.ai.neo4jgraphdbrag.nodes;

import java.util.List;
import org.springframework.data.neo4j.core.schema.*;

@RelationshipProperties
public class Acted_In {
  @Id @GeneratedValue String id;
  List<String> roles;
  @TargetNode private Person actor;

  public Acted_In(Person actor, List<String> roles) {
    this.actor = actor;
    this.roles = roles;
  }

  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }

  public Person getActor() {
    return actor;
  }

  public void setActor(Person actor) {
    this.actor = actor;
  }
}
