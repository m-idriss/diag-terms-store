package com.dime.term;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Term extends PanacheMongoEntity {

    public String word;
    public String description;
    public List<String> synonyms;

}