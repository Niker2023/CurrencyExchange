package org.petproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.petproject.dto.CurrencyDto;
import org.petproject.entity.Currency;

@Mapper
public interface DtoToCurrencyMapper {
    DtoToCurrencyMapper INSTANCE = Mappers.getMapper(DtoToCurrencyMapper.class);
    Currency toCurrency(CurrencyDto dto);
}
