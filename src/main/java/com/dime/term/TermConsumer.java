package com.dime.term;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import io.smallrye.reactive.messaging.kafka.Record;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.Collection;
import java.util.Collections;

@ApplicationScoped
public class TermConsumer {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Incoming("terms-in")
  public void receive(Record<String, String> record) {
    if (record == null || record.value() == null) {
      Log.warn("Received null or empty record from Kafka.");
      return;
    }

    try {
      Log.infof("Received term from Kafka: %s", record.value());
      Term term = objectMapper.readValue(record.value(), Term.class);
      storeTerm(term);
    } catch (JsonProcessingException e) {
      Log.error("Failed to deserialize term record: " + record.value(), e);
    } catch (Exception e) {
      Log.error("Unexpected error while processing term record", e);
    }
  }

  private void storeTerm(Term term) {
    if (term == null || term.word == null || term.description == null) {
      Log.warn("Invalid term object. Skipping persistence.");
      return;
    }

    try {
      Log.infof("Storing term to MongoDB: word=%s, description=%s, synonyms=%s",
          term.word, term.description, term.synonyms);
      Term.persist(term);
    } catch (Exception e) {
      Log.error("Failed to store term to MongoDB: word=" + term.word, e);
    }
  }

  public Collection<Term> getAllTerms() {
    Log.info("Fetching all terms from MongoDB.");
    try {
      return Term.listAll();
    } catch (Exception e) {
      Log.error("Failed to fetch terms from MongoDB", e);
      return Collections.emptyList();
    }
  }
}
