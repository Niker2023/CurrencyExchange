package org.petproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.petproject.dto.CurrencyDto;
import org.petproject.dto.ExchangeRateDto;
import org.petproject.entity.ExchangeRate;

@Mapper(uses = {CurrencyMapper.class})
public interface ExchangeRateMapper {
    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);
    ExchangeRateDto toExchangeRateDto(ExchangeRate rate);
    @Mapping(source = "ExchangeRate.baseCurrencyId", target = "CurrencyDto")
    ExchangeRate toExchangeRate(ExchangeRateDto dto);
}
