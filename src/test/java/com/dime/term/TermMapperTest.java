package com.dime.term;

import com.dime.model.TermRecord;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class TermMapperTest {

  @Test
  void testToRecord() {
    Term term = new Term();
    term.setId(1L);
    term.setWord("word");
    term.setSynonyms(List.of("synonym1", "synonym2"));

    TermRecord termRecord = TermMapper.INSTANCE.toRecord(term);

    assertEquals(term.getId(), termRecord.getId());
    assertEquals(term.getWord(), termRecord.getWord());
    assertEquals(term.getSynonyms(), termRecord.getSynonyms());
  }

  @Test
  void testToEntity() {
    TermRecord termRecord = new TermRecord();
    termRecord.setId(1L);
    termRecord.setWord("word");
    termRecord.setSynonyms(List.of("synonym1", "synonym2"));

    Term term = TermMapper.INSTANCE.toEntity(termRecord);

    assertEquals(termRecord.getId(), term.getId());
    assertEquals(termRecord.getWord(), term.getWord());
    assertEquals(termRecord.getSynonyms(), term.getSynonyms());
  }

}
