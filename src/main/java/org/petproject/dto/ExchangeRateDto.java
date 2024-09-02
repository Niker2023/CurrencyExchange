package org.petproject.dto;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class ExchangeRateDto {

    int id;
    CurrencyDto baseCurrency;
    CurrencyDto targetCurrency;
    double rate;

}
