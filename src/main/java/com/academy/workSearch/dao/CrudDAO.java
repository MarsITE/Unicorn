package com.academy.workSearch.dao;



import java.util.List;
import java.util.UUID;

public interface CrudDAO<E> {

    List<E> findAll();

    void save(E e);

    E get(UUID id);

    void delete(UUID id);
}
