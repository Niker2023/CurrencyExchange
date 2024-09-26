package org.petproject.service;

import org.petproject.dao.CurrencyDao;
import org.petproject.dto.CurrencyDto;
import org.petproject.mapper.DtoToCurrencyMapper;
import org.petproject.mapper.CurrencyToDtoMapper;

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
                    .map(CurrencyToDtoMapper.INSTANCE::toDto)
                    .collect(Collectors.toList());
    }


    public CurrencyDto save(CurrencyDto currencyDto) throws SQLException {
        var currency = DtoToCurrencyMapper.INSTANCE.toCurrency(currencyDto);
        var saved = currencyDao.save(currency);
        return CurrencyToDtoMapper.INSTANCE.toDto(saved);
    }


    public Optional<CurrencyDto> getByCode(String code) throws SQLException {
        var currencyByCode = currencyDao.getByCode(code);
        if (currencyByCode.isPresent()) {
            var currentCurrency = currencyByCode.get();
            return Optional.of(CurrencyToDtoMapper.INSTANCE.toDto(currentCurrency));
        }
        return Optional.empty();
    }
}
