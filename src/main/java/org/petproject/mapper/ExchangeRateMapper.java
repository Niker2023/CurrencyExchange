package org.petproject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.petproject.dto.CurrencyDto;
import org.petproject.dto.ExchangeRateDto;
import org.petproject.entity.ExchangeRate;
import org.petproject.service.CurrencyService;

import java.sql.SQLException;

@Mapper(uses = {CurrencyMapper.class})
public interface ExchangeRateMapper {
    ExchangeRateMapper INSTANCE = Mappers.getMapper(ExchangeRateMapper.class);


    @Mapping(source = "baseCurrency", target = "baseCurrencyId", qualifiedByName = "getCurrencyIdByCurrency")
    @Mapping(source = "targetCurrency", target = "targetCurrencyId", qualifiedByName = "getCurrencyIdByCurrency")
    ExchangeRate toExchangeRate(ExchangeRateDto dto);

    @Mapping(source = "baseCurrencyId", target = "baseCurrency", qualifiedByName = "getCurrencyDtoById")
    @Mapping(source = "targetCurrencyId", target = "targetCurrency", qualifiedByName = "getCurrencyDtoById")
    ExchangeRateDto toExchangeRateDto(ExchangeRate exchangeRate) throws SQLException;


    @Named("getCurrencyIdByCurrency")
    public static int getCurrencyIdByCurrency(CurrencyDto currency) {
        return currency.getId();
    }


    @Named("getCurrencyDtoById")
    public static CurrencyDto getCurrencyDtoById(int id) throws SQLException {
        var currencyDtoById = CurrencyService.getInstance().getCurrencyDtoById(id);
        if (currencyDtoById.isPresent()) {
            return currencyDtoById.get();
        }
        throw new SQLException("Currency not found");
    }
}
