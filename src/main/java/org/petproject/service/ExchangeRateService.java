package org.petproject.service;

import org.petproject.dao.CurrencyDao;
import org.petproject.dao.ExchangeRateDao;
import org.petproject.dto.CurrencyDto;
import org.petproject.dto.ExchangeRateDto;
import org.petproject.entity.Currency;
import org.petproject.entity.ExchangeRate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExchangeRateService {

    private static final ExchangeRateService INSTANCE = new ExchangeRateService();

    private final ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();

    private final CurrencyDao currencyDao = CurrencyDao.getInstance();

    public static ExchangeRateService getInstance() {
        return INSTANCE;
    }

    private ExchangeRateService() {}

    public List<ExchangeRateDto> findAll() {
        return exchangeRateDao.getAll().stream()
                .map(exchangeRate -> new ExchangeRateDto(
                        exchangeRate.getId(),
                        getCurrencyDtoById(exchangeRate.getBaseCurrencyId()),
                        getCurrencyDtoById(exchangeRate.getTargetCurrencyId()),
                        exchangeRate.getRate()
                ))
                .collect(Collectors.toList());
    }


    private CurrencyDto getCurrencyDtoById(int id) {
        Currency currency = currencyDao.getById(id).get();
        return new CurrencyDto(currency.getId(), currency.getName(), currency.getCode(), currency.getSign());
    }


    public ExchangeRateDto findByCodes(String baseCurrencyCode, String targetCurrencyCode) {
        Currency baseCurrency = currencyDao.getByCode(baseCurrencyCode).get();
        Currency targetCurrency = currencyDao.getByCode(targetCurrencyCode).get();
        Optional<ExchangeRate> exchangeRate = exchangeRateDao.getByIds(baseCurrency.getId(), targetCurrency.getId());

        return exchangeRate.map(rate -> new ExchangeRateDto(rate.getId(),
                new CurrencyDto(baseCurrency.getId(), baseCurrency.getName(), baseCurrency.getCode(), baseCurrency.getSign()),
                new CurrencyDto(targetCurrency.getId(), targetCurrency.getName(), targetCurrency.getCode(), targetCurrency.getSign()),
                rate.getRate())).orElse(null);
    }


    public ExchangeRateDto save (ExchangeRateDto exchangeRateDto) {
        var exchangeRate = new ExchangeRate(exchangeRateDto.getId(),
                exchangeRateDto.getBaseCurrency().getId(),
                exchangeRateDto.getTargetCurrency().getId(),
                exchangeRateDto.getRate());

        var savedExchangeRate = exchangeRateDao.save(exchangeRate);

        return new ExchangeRateDto(savedExchangeRate.getId(),
                getCurrencyDtoById(savedExchangeRate.getBaseCurrencyId()),
                getCurrencyDtoById(savedExchangeRate.getTargetCurrencyId()),
                savedExchangeRate.getRate());


    }


    public ExchangeRateDto update (ExchangeRateDto exchangeRateDto) {
        var exchangeRate = new ExchangeRate(exchangeRateDto.getId(),
                exchangeRateDto.getBaseCurrency().getId(),
                exchangeRateDto.getTargetCurrency().getId(),
                exchangeRateDto.getRate());

        var updatedExchangeRate = exchangeRateDao.update(exchangeRate);

        return new ExchangeRateDto(updatedExchangeRate.getId(),
                getCurrencyDtoById(updatedExchangeRate.getBaseCurrencyId()),
                getCurrencyDtoById(updatedExchangeRate.getTargetCurrencyId()),
                updatedExchangeRate.getRate());
    }
}
