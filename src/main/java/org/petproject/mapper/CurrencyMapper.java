package org.petproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.petproject.dto.CurrencyDto;
import org.petproject.entity.Currency;

@Mapper
public interface CurrencyMapper {
    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    CurrencyDto toDto(Currency currency);

    Currency fromDto(CurrencyDto dto);
}
