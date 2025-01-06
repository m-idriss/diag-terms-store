package com.dime.term;

import com.dime.model.TermRecord;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.CDI)
public interface TermMapper {

  TermMapper INSTANCE = Mappers.getMapper(TermMapper.class);

  Term toEntity(TermRecord termRecord);

  TermRecord toRecord(Term term);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Term partialUpdate(TermRecord termRecord, @MappingTarget Term term);
}