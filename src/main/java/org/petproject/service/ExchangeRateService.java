package org.petproject.service;

import org.petproject.dao.CurrencyDao;
import org.petproject.dao.ExchangeRateDao;
import org.petproject.entity.dto.CurrencyDto;
import org.petproject.entity.dto.ExchangeAmountDto;
import org.petproject.entity.dto.ExchangeRateDto;
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

    private ExchangeRateService() {
    }

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


    public ExchangeRateDto save(ExchangeRateDto exchangeRateDto) {
        var exchangeRate = new ExchangeRate(exchangeRateDto.getId(),
                exchangeRateDto.getBaseCurrency().getId(),
                exchangeRateDto.getTargetCurrency().getId(),
                exchangeRateDto.getRate());

        var savedExchangeRate = exchangeRateDao.save(exchangeRate);
        if (savedExchangeRate.isPresent()) {
            return new ExchangeRateDto(savedExchangeRate.get().getId(),
                    getCurrencyDtoById(savedExchangeRate.get().getBaseCurrencyId()),
                    getCurrencyDtoById(savedExchangeRate.get().getTargetCurrencyId()),
                    savedExchangeRate.get().getRate());
        }
        return null;
    }



    public ExchangeRateDto update(ExchangeRateDto exchangeRateDto) {
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


    public Optional<ExchangeAmountDto> exchangeAmount(ExchangeAmountDto exchangeAmountDto) {

        Optional<ExchangeAmountDto> result;
        if ((result = exchangeRateBaseToTarget(exchangeAmountDto)).isPresent()) {
            return result;
        } else if ((result = exchangeRateTargetToBase(exchangeAmountDto)).isPresent()) {
            return result;
        } else if ((result = exchangeViaUsd(exchangeAmountDto)).isPresent()) {
            return result;
        }
        return result;
    }

    private Optional<ExchangeAmountDto> exchangeRateBaseToTarget(ExchangeAmountDto exchangeAmountDto) {
        Optional<ExchangeAmountDto> result = Optional.empty();
        var exchangeRateBaseToTarget = exchangeRateDao.getByIds(
                exchangeAmountDto.getBaseCurrency().getId(),
                exchangeAmountDto.getTargetCurrency().getId());
        if (exchangeRateBaseToTarget.isPresent()) {
            result = Optional.of(new ExchangeAmountDto(exchangeAmountDto.getBaseCurrency(),
                    exchangeAmountDto.getTargetCurrency(),
                    exchangeAmountDto.getExchangeAmount(),
                    exchangeRateBaseToTarget.get().getRate(),
                    exchangeRateBaseToTarget.get().getRate() * exchangeAmountDto.getExchangeAmount()));
        }
        return result;
    }


    private Optional<ExchangeAmountDto> exchangeRateTargetToBase(ExchangeAmountDto exchangeAmountDto) {
        Optional<ExchangeAmountDto> result = Optional.empty();
        var exchangeRateTargetToBase = exchangeRateDao.getByIds(
                exchangeAmountDto.getTargetCurrency().getId(),
                exchangeAmountDto.getBaseCurrency().getId());
        if (exchangeRateTargetToBase.isPresent()) {
            result = Optional.of(new ExchangeAmountDto(exchangeAmountDto.getBaseCurrency(),
                    exchangeAmountDto.getTargetCurrency(),
                    exchangeRateTargetToBase.get().getRate(),
                    exchangeAmountDto.getExchangeAmount(),
                    1 / exchangeRateTargetToBase.get().getRate() * exchangeAmountDto.getExchangeAmount()));
        }
        return result;
    }

    private Optional<ExchangeAmountDto> exchangeViaUsd(ExchangeAmountDto exchangeAmountDto) {
        Optional<ExchangeAmountDto> result = Optional.empty();
        var usd = currencyDao.getByCode("USD").get();
        var exchangeRateUsdToBase = exchangeRateDao.getByIds(usd.getId(),
                exchangeAmountDto.getBaseCurrency().getId());
        var exchangeRateUsdToTarget = exchangeRateDao.getByIds(usd.getId(),
                exchangeAmountDto.getTargetCurrency().getId());
        if (exchangeRateUsdToBase.isPresent() && exchangeRateUsdToTarget.isPresent()) {
            result = Optional.of(new ExchangeAmountDto(exchangeAmountDto.getBaseCurrency(),
                    exchangeAmountDto.getTargetCurrency(),
                    1/exchangeRateUsdToBase.get().getRate() * exchangeRateUsdToTarget.get().getRate(),
                    exchangeAmountDto.getExchangeAmount(),
                    1/exchangeRateUsdToBase.get().getRate() * exchangeRateUsdToTarget.get().getRate() *
                            exchangeAmountDto.getExchangeAmount()));
        }
        return result;
    }
}