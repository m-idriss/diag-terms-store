package com.dime.term;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TermRepository {

  private final MongoCollection<Term> coll;

  public TermRepository(MongoClient mongoClient) {
    this.coll = mongoClient.getDatabase("term").getCollection("term", Term.class);
  }

  public void create(Term term) {
    Log.infof("Storing term to MongoDB: word=%s, synonyms=%s",
        term.getWord(), term.getSynonyms());
    coll.insertOne(term);
  }

  public List<Term> getAllTerms() {
    Log.infof("Retrieving all terms from MongoDB");
    return coll.find().into(new ArrayList<>());
  }

}
