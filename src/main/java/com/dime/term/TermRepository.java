package com.dime.term;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TermRepository implements PanacheRepository<Term> {

  /**
   * This method returns a term persisted in the database.
   * if word match any of the word or other_forms in the database ignoring case.
   */
  public Term findByWord(String word) {
    return find("LOWER(word) = ?1 OR ?1 MEMBER OF otherForms", word.toLowerCase()).firstResult();
  }

  /**
   * This method deletes a term persisted in the database by its word.
   */
  public long deleteByWord(String word) {
    return delete("word", word);
  }
}