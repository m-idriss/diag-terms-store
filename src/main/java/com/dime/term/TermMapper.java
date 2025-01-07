package com.dime.term;

import com.dime.model.TermRecord;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.CDI)
public interface TermMapper {

  TermMapper INSTANCE = Mappers.getMapper(TermMapper.class);

  @Mapping(target = "word", source = "word", qualifiedByName = "lowercase")
  Term toEntity(TermRecord termRecord);

  @Mapping(target = "word", source = "word", qualifiedByName = "lowercase")
  TermRecord toRecord(Term term);

  @Named("lowercase")
  default String lowercase(String word) {
    return word.toLowerCase();
  }
}