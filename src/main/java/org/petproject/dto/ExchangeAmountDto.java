package org.petproject.dto;

import lombok.Value;

@Value
public class ExchangeAmountDto {

    CurrencyDto baseCurrency;
    CurrencyDto targetCurrency;
    double exchangeRate;
    double exchangeAmount;
    double convertedAmount;

}
