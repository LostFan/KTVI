package org.lostfan.ktv.dao;

import java.util.List;

import org.lostfan.ktv.domain.Entity;

/**
 * Created by Ihar_Niakhlebau on 05-Nov-15.
 */
public interface EntityDAO<T extends Entity> {

    List<T> getAll();

    T get(int id);

    T save(T t);

    T update(T t);

    void delete(int id);

    List<T> getAllContainsInName(String str);
}
