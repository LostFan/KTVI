package org.lostfan.ktv.dao;

import java.util.List;

import org.lostfan.ktv.domain.Entity;

public interface EntityDAO<T extends Entity> extends Transactional {

    List<T> getAll();

    T get(int id);

    void save(T t);

    void update(T t);

    void delete(int id);

    List<T> getAllContainsInName(String str);
}
