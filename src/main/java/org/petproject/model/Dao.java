package org.petproject.model;

import java.util.List;
import java.util.Optional;

public interface Dao {

    Optional<Currency> get(int id);

    List<Currency> getAll();

    Currency save(Currency currency);

    void update(Currency currency, String[] params);

}
