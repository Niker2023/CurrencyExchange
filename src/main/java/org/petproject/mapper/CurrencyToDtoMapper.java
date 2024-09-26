package org.petproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.petproject.dto.CurrencyDto;
import org.petproject.entity.Currency;

@Mapper
public interface CurrencyToDtoMapper {
    CurrencyToDtoMapper INSTANCE = Mappers.getMapper(CurrencyToDtoMapper.class);
    CurrencyDto toDto(Currency currency);
}
