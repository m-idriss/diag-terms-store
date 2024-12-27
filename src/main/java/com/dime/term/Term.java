package com.dime.term;

import lombok.Data;

import java.util.List;

@Data
public class Term {

  private Long id;

  private String word;

  private List<String> synonyms;
}