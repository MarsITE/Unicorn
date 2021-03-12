package com.academy.workSearch.service;

import java.util.List;
import java.util.UUID;

public interface CrudService<E> {

    List<E> findAll();

    void save(E e);

    E get(UUID id);

    void delete(UUID id);
}
