package com.dime.term;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class TermRepositoryTest {

  @Inject
  TermRepository termRepository;

  @Test
  @TestTransaction
  void testFindByWord() {
    TermEntity term = new TermEntity();
    term.setWord("example");
    termRepository.persist(term);

    TermEntity found = termRepository.findByWord("example");

    assertNotNull(found);
    assertEquals("example", found.getWord());
  }

  @Test
  @TestTransaction
  void testDeleteByWord() {
    TermEntity term = new TermEntity();
    term.setWord("example");
    termRepository.persist(term);

    long deletedCount = termRepository.deleteByWord("example");

    assertEquals(1, deletedCount);

    TermEntity found = termRepository.findByWord("example");
    assertNull(found);
  }

  @Test
  @TestTransaction
  void testListAll() {
    TermEntity term1 = new TermEntity();
    term1.setWord("example1");
    TermEntity term2 = new TermEntity();
    term2.setWord("example2");

    termRepository.persist(term1);
    termRepository.persist(term2);

    List<TermEntity> terms = termRepository.listAll();

    assertEquals(2, terms.size());
    assertTrue(terms.stream().anyMatch(t -> "example1".equals(t.getWord())));
    assertTrue(terms.stream().anyMatch(t -> "example2".equals(t.getWord())));
  }
}
