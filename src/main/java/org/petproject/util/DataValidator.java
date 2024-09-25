package org.petproject.util;

public class DataValidator {

    public static boolean isCurrencyCodeNotValid(String code) {
        return code == null || code.length() != 3 || !code.matches("^[A-Z]+$");
    }
}
