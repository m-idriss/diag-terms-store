package com.dime.term;

import com.dime.model.TermRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import io.smallrye.reactive.messaging.kafka.Record;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.List;

@ApplicationScoped
public class TermConsumer {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Inject
  TermRepository termRepository;

  /**
   * This method is called whenever a new term is received from the Kafka topic.
   */
  @Transactional
  @Incoming("terms-store")
  public void receive(Record<String, String> termRecord) {
    if (termRecord == null || termRecord.value() == null) {
      Log.warn("Received null or empty record from Kafka.");
      return;
    }

    try {
      Log.infof("Received term from Kafka: %s", termRecord.value());
      TermRecord term = objectMapper.readValue(termRecord.value(), TermRecord.class);

      TermEntity termEntity = TermMapper.INSTANCE.toEntity(term);

      // If the word in the record is different from the word in the term object,
      // add the word to the forms list.
      String word = termRecord.key();
      if (!word.equalsIgnoreCase(termEntity.getWord())) {
        termEntity.setOtherForms(List.of(word.toLowerCase()));
      }

      storeTerm(termEntity);
    } catch (JsonProcessingException e) {
      Log.error("Failed to deserialize term record: " + termRecord.value(), e);
    } catch (Exception e) {
      Log.error("Unexpected error while processing term record", e);
    }
  }

  /**
   * This method persists the term to the database.
   */
  private void storeTerm(TermEntity termEntity) {
    if (termEntity == null || termEntity.getWord() == null) {
      Log.warn("Invalid term object. Skipping persistence.");
      return;
    }

    try {
      Log.infof("Storing term to DB: word=%s, synonyms=%s",
          termEntity.getWord(), termEntity.getSynonyms());
      termRepository.persist(termEntity);
      Log.infof("Term stored to DB: word=%s", termEntity.getWord());
    } catch (Exception e) {
      Log.error("Failed to store term to DB: word=" + termEntity.getWord(), e);
    }
  }
}
