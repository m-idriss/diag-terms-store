package com.dime.term;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import io.smallrye.reactive.messaging.kafka.Record;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class TermConsumer {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Inject
  TermRepository termRepository;

  @Incoming("terms-in")
  public void receive(Record<String, String> termRecord) {
    if (termRecord == null || termRecord.value() == null) {
      Log.warn("Received null or empty record from Kafka.");
      return;
    }

    try {
      Log.infof("Received term from Kafka: %s", termRecord.value());
      Term term = objectMapper.readValue(termRecord.value(), Term.class);
      storeTerm(term);
    } catch (JsonProcessingException e) {
      Log.error("Failed to deserialize term record: " + termRecord.value(), e);
    } catch (Exception e) {
      Log.error("Unexpected error while processing term record", e);
    }
  }

  private void storeTerm(Term term) {
    if (term == null || term.getWord() == null) {
      Log.warn("Invalid term object. Skipping persistence.");
      return;
    }

    try {
      Log.infof("Storing term to DB: word=%s, synonyms=%s",
          term.getWord(), term.getSynonyms());
      termRepository.persist(term);
      Log.infof("Term stored to DB: word=%s", term.getWord());
    } catch (Exception e) {
      Log.error("Failed to store term to DB: word=" + term.getWord(), e);
    }
  }

}
