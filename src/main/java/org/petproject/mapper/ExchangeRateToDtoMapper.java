package org.petproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.petproject.dto.ExchangeRateDto;
import org.petproject.entity.ExchangeRate;

@Mapper
public interface ExchangeRateToDtoMapper {
    ExchangeRateToDtoMapper INSTANCE = Mappers.getMapper(ExchangeRateToDtoMapper.class);
    ExchangeRateDto toExchangeRateDto(ExchangeRate rate);
}
