package org.petproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.petproject.dto.ExchangeRateDto;
import org.petproject.entity.ExchangeRate;

@Mapper
public interface DtoToExchangeRateMapper {
    DtoToExchangeRateMapper INSTANCE = Mappers.getMapper(DtoToExchangeRateMapper.class);
    ExchangeRate toExchangeRate(ExchangeRateDto dto);
}
