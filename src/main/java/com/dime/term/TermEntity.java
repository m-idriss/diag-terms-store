package com.dime.term;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity(name = "term")
@Setter
@Getter
public class TermEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonProperty("id")
  private Long id;

  @Column(unique = true)
  @JsonProperty("word")
  private String word;

  @JsonProperty("synonyms")
  @ElementCollection(fetch = FetchType.EAGER)
  private List<String> synonyms;

  @JsonProperty("other_forms")
  @ElementCollection(fetch = FetchType.EAGER)
  private List<String> otherForms;
}