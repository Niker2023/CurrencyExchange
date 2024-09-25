package org.petproject.service;

import org.petproject.dao.CurrencyDao;
import org.petproject.dto.CurrencyDto;
import org.petproject.entity.Currency;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
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


    public CurrencyDto save(CurrencyDto currencyDto) throws SQLException {
        var currency = new Currency(currencyDto.getId(), currencyDto.getCode(), currencyDto.getName(), currencyDto.getSign());
        var saved = currencyDao.save(currency);
        return new CurrencyDto(saved.getId(), saved.getCode(), saved.getName(), saved.getSign());
    }


    public Optional<CurrencyDto> getByCode(String code) throws SQLException {
        var currencyByCode = currencyDao.getByCode(code);
        if (currencyByCode.isPresent()) {
            var currentCurrency = currencyByCode.get();
            return Optional.of(new CurrencyDto(currentCurrency.getId(), currentCurrency.getName(), currentCurrency.getCode(), currentCurrency.getSign()));
        }
        return Optional.empty();
    }
}
