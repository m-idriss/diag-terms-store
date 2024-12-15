package com.dime.term;

import java.util.Collection;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.logging.Log;
import io.smallrye.reactive.messaging.kafka.Record;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TermProducer {

    @Inject
    @Channel("terms-out")
    Emitter<Record<String, String>> emitter;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void sendToKafka(Term term) {
        if (term == null || term.word == null || term.description == null) {
            throw new IllegalArgumentException("Term or its properties cannot be null");
        }
        try {
            String termJson = termToJson(term);
            Log.infof("Sending term to Kafka: %s", termJson);
            emitter.send(Record.of(term.word, termJson));
            Log.infof("Term sent to Kafka: %s", termJson);
        } catch (RuntimeException e) {
            Log.error("Failed to send term to Kafka", e);
        }
    }

    private String termToJson(Term term) {
        try {
            return objectMapper.writeValueAsString(term);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize Term object to JSON", e);
        }
    }

    public Collection<Term> getAllTerms() {
        return new TermConsumer().getAllTerms();
    }
}
