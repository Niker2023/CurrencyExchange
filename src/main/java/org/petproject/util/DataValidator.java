package org.petproject.util;

public class DataValidator {

    public static boolean isCurrencyCodeNotValid(String code) {
        return code == null || code.length() != 3 || !code.matches("^[A-Z]+$");
    }


    public static boolean isCurrenciesCodesNotValid(String code) {
        return code == null || code.length() != 6 || !code.matches("^[A-Z]+$");
    }


    public static boolean isExchangeRateNotValid(String rate) {
        return rate == null || !(rate.matches("^[0-9]+(\\.[0-9]{1,6})?$") && Double.parseDouble(rate) != 0);
    }


    public static boolean isAmountNotValid(String amount) {
        return amount == null || !(amount.matches("^[0-9]+(\\.[0-9]{1,2})?$") && Double.parseDouble(amount) != 0);
    }
}
