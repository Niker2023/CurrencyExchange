package org.petproject.model;

import java.util.List;
import java.util.Optional;

public class CurrencyDao implements Dao{
    @Override
    public Optional<Currency> get(int id) {
        return Optional.empty();
    }

    @Override
    public List<Currency> getAll() {
        return null;
    }

    @Override
    public void save(Currency currency) {

    }

    @Override
    public void update(Currency currency, String[] params) {

    }
}
