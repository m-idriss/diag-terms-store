package com.dime.term;

import com.dime.model.TermRecord;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TermMapper {

  public TermRecord toRecord(Term term) {
    TermRecord termRecord = new TermRecord();
    termRecord.setId(term.getId());
    termRecord.setWord(term.getWord().toLowerCase());
    termRecord.setSynonyms(term.getSynonyms().stream().map(String::toLowerCase).toList());
    return termRecord;
  }

  public Term toEntity(TermRecord termRecord) {
    Term term = new Term();
    term.setId(termRecord.getId());
    term.setWord(termRecord.getWord().toLowerCase());
    term.setSynonyms(termRecord.getSynonyms().stream().map(String::toLowerCase).toList());
    return term;
  }

}
