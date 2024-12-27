package com.dime.term;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;

@Data
public class Term {

  private ObjectId id;

  private String word;

  private List<String> synonyms;
}