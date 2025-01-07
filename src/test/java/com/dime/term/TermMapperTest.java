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
    TermEntity termEntity = new TermEntity();
    termEntity.setId(1L);
    termEntity.setWord("word");
    termEntity.setSynonyms(List.of("synonym1", "synonym2"));

    TermRecord termRecord = TermMapper.INSTANCE.toRecord(termEntity);

    assertEquals(termEntity.getId(), termRecord.getId());
    assertEquals(termEntity.getWord(), termRecord.getWord());
    assertEquals(termEntity.getSynonyms(), termRecord.getSynonyms());
  }

  @Test
  void testToEntity() {
    TermRecord termRecord = new TermRecord();
    termRecord.setId(1L);
    termRecord.setWord("word");
    termRecord.setSynonyms(List.of("synonym1", "synonym2"));

    TermEntity termEntity = TermMapper.INSTANCE.toEntity(termRecord);

    assertEquals(termRecord.getId(), termEntity.getId());
    assertEquals(termRecord.getWord(), termEntity.getWord());
    assertEquals(termRecord.getSynonyms(), termEntity.getSynonyms());
  }

}
