package com.dime.term;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TermService {

  @Inject
  private TermRepository termRepository;

  /*
   * This method returns a list of all terms persisted in the database by id ascending order
   */
  public List<TermEntity> listAll() {
    return termRepository.listAll(Sort.by("id"));
  }

  /*
   * This method deletes a term from the database by its word
   */
  public Optional<Boolean> deleteByWord(String word) {
    return Optional.of(termRepository.deleteByWord(word) > 0);
  }

  /*
   * This method returns a term by its id
   */

  public Optional<TermEntity> findById(Long id) {
    return Optional.ofNullable(termRepository.findById(id));
  }

  /*
   * This method returns a term by its word
   */
  public Optional<TermEntity> findByWord(String word) {
    return Optional.ofNullable(termRepository.findByWord(word));
  }

  /*
   * This method creates a term in the database
   */
  public TermEntity create(TermEntity term) {
    termRepository.persist(term);
    return termRepository.findByWord(term.getWord());
  }

  /*
   * This method delete a term in the database
   */
  public Optional<TermEntity> deleteById(Long id) {
    Optional<TermEntity> term = findById(id);
    term.ifPresent(termRepository::delete);
    return term;
  }
}
