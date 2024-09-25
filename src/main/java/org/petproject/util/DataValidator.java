package org.petproject.util;

public class DataValidator {

    public static boolean isCurrencyCodeNotValid(String code) {
        return code == null || code.length() != 3 || !code.matches("^[A-Z]+$");
    }


    public static boolean isCurrenciesCodesNotValid(String code) {
        return code == null || code.length() != 6 || !code.matches("^[A-Z]+$");
    }


    public static boolean isExchangeRateNotValid(String rate) {
        return !rate.matches("^[0-9]+\\.?[0-9]{0,6}$");
    }
}
