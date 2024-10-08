package org.petproject.service;

import org.petproject.dao.CurrencyDao;
import org.petproject.dao.ExchangeRateDao;
import org.petproject.dto.ExchangeAmountDto;
import org.petproject.dto.ExchangeRateDto;
import org.petproject.entity.ExchangeRate;
import org.petproject.mapper.CurrencyMapper;
import org.petproject.mapper.ExchangeRateMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateService {

    private static final ExchangeRateService INSTANCE = new ExchangeRateService();

    private final ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();

    private final CurrencyDao currencyDao = CurrencyDao.getInstance();

    public static ExchangeRateService getInstance() {
        return INSTANCE;
    }


    private ExchangeRateService() {
    }

    public List<ExchangeRateDto> findAll() throws SQLException {
            List<ExchangeRateDto> collect = new ArrayList<>();
            for (ExchangeRate exchangeRate : exchangeRateDao.getAll()) {
                ExchangeRateDto exchangeRateDto = ExchangeRateMapper.INSTANCE.toExchangeRateDto(exchangeRate);
                collect.add(exchangeRateDto);
            }
        return collect;
    }


    public Optional<ExchangeRateDto> findByCodes(String baseCurrencyCode, String targetCurrencyCode) throws SQLException {
        var baseCurrencyByCode = currencyDao.getByCode(baseCurrencyCode);
        var targetCurrencyByCode = currencyDao.getByCode(targetCurrencyCode);

        if (baseCurrencyByCode.isPresent() && targetCurrencyByCode.isPresent()) {
            var baseCurrency = baseCurrencyByCode.get();
            var targetCurrency = targetCurrencyByCode.get();
            Optional<ExchangeRate> exchangeRate = exchangeRateDao.getByIds(baseCurrency.getId(), targetCurrency.getId());
            if (exchangeRate.isPresent()) {
                return exchangeRate.map(rate -> new ExchangeRateDto(rate.getId(),
                        CurrencyMapper.INSTANCE.toDto(baseCurrency),
                        CurrencyMapper.INSTANCE.toDto(targetCurrency),
                        rate.getRate()));
            }
        }
        return Optional.empty();
    }


    public Optional<ExchangeRateDto> save(ExchangeRateDto exchangeRateDto) throws SQLException {
        var exchangeRate = ExchangeRateMapper.INSTANCE.toExchangeRate(exchangeRateDto);
        var savedExchangeRate = exchangeRateDao.save(exchangeRate);
        if (savedExchangeRate.isPresent()) {
            var rateDto = ExchangeRateMapper.INSTANCE.toExchangeRateDto(savedExchangeRate.get());
            return Optional.of(rateDto);
        }
        return Optional.empty();
    }


    public Optional<ExchangeRateDto> update(ExchangeRateDto exchangeRateDto) throws SQLException {
        var exchangeRate = new ExchangeRate(exchangeRateDto.getId(),
                exchangeRateDto.getBaseCurrency().getId(),
                exchangeRateDto.getTargetCurrency().getId(),
                exchangeRateDto.getRate());
        var updatedExchangeRate = exchangeRateDao.update(exchangeRate);
        if (updatedExchangeRate.isPresent()) {
            var rateDto = ExchangeRateMapper.INSTANCE.toExchangeRateDto(updatedExchangeRate.get());
            return Optional.of(rateDto);
        }
        return Optional.empty();
    }

    public Optional<ExchangeAmountDto> exchangeAmount(ExchangeAmountDto exchangeAmountDto) throws SQLException {
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

    private Optional<ExchangeAmountDto> exchangeRateBaseToTarget(ExchangeAmountDto exchangeAmountDto) throws SQLException {
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


    private Optional<ExchangeAmountDto> exchangeRateTargetToBase(ExchangeAmountDto exchangeAmountDto) throws SQLException {
        Optional<ExchangeAmountDto> result = Optional.empty();
        var exchangeRateTargetToBase = exchangeRateDao.getByIds(
                exchangeAmountDto.getTargetCurrency().getId(),
                exchangeAmountDto.getBaseCurrency().getId());
        if (exchangeRateTargetToBase.isPresent()) {
            result = Optional.of(new ExchangeAmountDto(exchangeAmountDto.getBaseCurrency(),
                    exchangeAmountDto.getTargetCurrency(),
                    1 / exchangeRateTargetToBase.get().getRate(),
                    exchangeAmountDto.getExchangeAmount(),
                    1 / exchangeRateTargetToBase.get().getRate() * exchangeAmountDto.getExchangeAmount()));
        }
        return result;
    }

    private Optional<ExchangeAmountDto> exchangeViaUsd(ExchangeAmountDto exchangeAmountDto) throws SQLException{
        Optional<ExchangeAmountDto> result = Optional.empty();
        var usd = currencyDao.getByCode("USD");
        if (usd.isEmpty()) {
            return result;
        }
        var exchangeRateUsdToBase = exchangeRateDao.getByIds(usd.get().getId(),
                exchangeAmountDto.getBaseCurrency().getId());
        var exchangeRateUsdToTarget = exchangeRateDao.getByIds(usd.get().getId(),
                exchangeAmountDto.getTargetCurrency().getId());
        if (exchangeRateUsdToBase.isPresent() && exchangeRateUsdToTarget.isPresent()) {
            result = Optional.of(new ExchangeAmountDto(exchangeAmountDto.getBaseCurrency(),
                    exchangeAmountDto.getTargetCurrency(),
                    1 / exchangeRateUsdToBase.get().getRate() * exchangeRateUsdToTarget.get().getRate(),
                    exchangeAmountDto.getExchangeAmount(),
                    1 / exchangeRateUsdToBase.get().getRate() * exchangeRateUsdToTarget.get().getRate() *
                            exchangeAmountDto.getExchangeAmount()));
        }
        return result;
    }
}