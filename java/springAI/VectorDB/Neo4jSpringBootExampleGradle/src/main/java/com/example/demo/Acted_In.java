package com.example.demo;

import org.springframework.data.neo4j.core.schema.*;

import java.util.List;

@RelationshipProperties
public class Acted_In {
  @Id @GeneratedValue String id;
  List<String> roles;
  @TargetNode private PersonEntity actor;

  public Acted_In(PersonEntity actor, List<String>  roles) {
    this.actor = actor;
    this.roles = roles;
  }

  public List<String>  getRoles() {
    return roles;
  }

  public void setRoles(List<String>  roles) {
    this.roles = roles;
  }

  public PersonEntity getActor() {
    return actor;
  }

  public void setActor(PersonEntity actor) {
    this.actor = actor;
  }
}
