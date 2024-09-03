package org.petproject.dto;

import lombok.Value;

@Value
public class ExchangeRateDto {

    int id;
    CurrencyDto baseCurrency;
    CurrencyDto targetCurrency;
    double rate;

}
