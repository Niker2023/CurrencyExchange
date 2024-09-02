package org.petproject.dao;

import org.petproject.entity.Currency;

import java.util.List;
import java.util.Optional;

public interface Dao<K, T> {

    Optional<T> getBy(K entity);

    List<T> getAll();

    T save(T entity);

    void update(T entity, String[] params);

}
