package org.petproject.dao;

import org.petproject.entity.Currency;

import java.util.List;
import java.util.Optional;

public interface Dao {

    Optional<Currency> getByCode(String code);

    List<Currency> getAll();

    Currency save(Currency currency);

    void update(Currency currency, String[] params);

}
