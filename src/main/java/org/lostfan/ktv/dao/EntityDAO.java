package org.lostfan.ktv.dao;

import java.util.List;

/**
 * Created by Ihar_Niakhlebau on 05-Nov-15.
 */
public interface EntityDAO<T> {

    List<T> getAll();

    T get(int id);

    void save(T t);

    void update(T t);

    void delete(int id);
}
