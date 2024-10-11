package org.petproject.dto;

import lombok.Value;

@Value
public class ExchangeAmountDto {

    CurrencyDto baseCurrency;
    CurrencyDto targetCurrency;
    String exchangeRate;
    String exchangeAmount;
    String convertedAmount;

}
