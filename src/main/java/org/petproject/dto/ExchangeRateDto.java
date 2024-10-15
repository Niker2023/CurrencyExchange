package org.petproject.dto;

import lombok.Value;

@Value
public class ExchangeRateDto {

    int id;
    CurrencyDto baseCurrency;
    CurrencyDto targetCurrency;
    double rate;


    public ExchangeRateDto(int id, CurrencyDto baseCurrency, CurrencyDto targetCurrency, double rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }
}
