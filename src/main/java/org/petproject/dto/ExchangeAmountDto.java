package org.petproject.dto;

import lombok.Value;

@Value
public class ExchangeAmountDto {

    String baseCurrency;
    String targetCurrency;
    double amount;
    double convertedAmount;

}
