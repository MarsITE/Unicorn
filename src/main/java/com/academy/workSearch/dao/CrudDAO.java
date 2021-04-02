package com.academy.workSearch.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CrudDAO<E> {

    List<E> findAll();

    E save(E e);

    Optional<E> get(UUID id);

    Optional<E> delete(UUID id);

    void setClazz(Class<E> clazz);
}
