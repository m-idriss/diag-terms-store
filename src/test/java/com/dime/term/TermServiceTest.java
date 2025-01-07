package com.dime.term;

import io.quarkus.panache.common.Sort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TermServiceTest {

  @Mock
  private TermRepository termRepository;

  @InjectMocks
  private TermService termService;

  private TermEntity term;

  @BeforeEach
  public void setup() {
    term = new TermEntity();
    term.setWord("example");
    List<String> synonyms = List.of("synonym1", "synonym2");
    term.setSynonyms(synonyms);
  }

  @Test
  void testListAll() {
    TermEntity term2 = new TermEntity();
    term2.setWord("example2");
    term2.setSynonyms(List.of("synonym3", "synonym4"));
    List<TermEntity> terms = List.of(term2, term, term2);

    when(termRepository.listAll(any(Sort.class))).thenReturn(terms);

    List<TermEntity> result = termService.listAll();

    assertEquals(terms, result);
    verify(termRepository, times(1)).listAll(any(Sort.class));
  }

  @Test
  void testDeleteByWord() {
    when(termRepository.deleteByWord("example")).thenReturn(1L);

    Optional<Boolean> result = termService.deleteByWord("example");

    assertTrue(result.isPresent());
    assertTrue(result.get());
    verify(termRepository, times(1)).deleteByWord("example");
  }

  @Test
  void testDeleteByWord_TermNotInDatabase() {
    when(termRepository.deleteByWord("example")).thenReturn(0L);

    Optional<Boolean> result = termService.deleteByWord("example");

    assertTrue(result.isPresent());
    assertFalse(result.get());
    verify(termRepository, times(1)).deleteByWord("example");
  }

  @Test
  void testGetTerm_TermInDatabase() {
    when(termRepository.findByWord("example")).thenReturn(term);

    Optional<TermEntity> result = termService.findByWord("example");

    assertTrue(result.isPresent());
    assertEquals(term, result.get());
    verify(termRepository, times(1)).findByWord("example");
    verify(termRepository, times(0)).persist(term);
  }

}
