package org.petproject.service;

import org.petproject.dao.CurrencyDao;
import org.petproject.entity.dto.CurrencyDto;
import org.petproject.entity.Currency;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class CurrencyService {

    private static final CurrencyService INSTANCE = new CurrencyService();

    private final CurrencyDao currencyDao = CurrencyDao.getInstance();

    public static CurrencyService getInstance() {
        return INSTANCE;
    }

    private CurrencyService() {}


    public List<CurrencyDto> findAll() throws SQLException {
            return currencyDao.getAll().stream()
                    .map(currency -> new CurrencyDto(
                            currency.getId(),
                            currency.getCode(),
                            currency.getName(),
                            currency.getSign()
                    ))
                    .collect(Collectors.toList());
    }


    public CurrencyDto save(CurrencyDto currencyDto) {
        var curreny = new Currency(currencyDto.getId(), currencyDto.getCode(), currencyDto.getName(), currencyDto.getSign());
        var saved = currencyDao.save(curreny);
        return new CurrencyDto(saved.getId(), saved.getCode(), saved.getName(), saved.getSign());
    }


    public CurrencyDto getByCode(String code) {
        var byCode = currencyDao.getByCode(code);
        var currentCurrency = byCode.get();
        return new CurrencyDto(currentCurrency.getId(), currentCurrency.getName(), currentCurrency.getCode(), currentCurrency.getSign());
    }


}
