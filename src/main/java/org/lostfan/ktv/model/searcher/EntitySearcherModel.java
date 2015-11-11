package org.lostfan.ktv.model.searcher;

import java.util.List;

import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.utils.BaseObservable;
import org.lostfan.ktv.utils.Observable;

public abstract class EntitySearcherModel<T extends Entity> extends BaseObservable {

    private List<T> entities;

    protected abstract EntityDAO<T> getDao();

    public List<T> getList() {
        if (this.entities == null) {
            this.entities = getDao().getAll();
        }

        return this.entities;
    }

    public void setSearchQuery(String query) {
        this.entities = getDao().getAllContainsInName(query);
        this.notifyObservers(null);
    }

    public T getEntity(int id) {

        return getDao().get(id);
    }

    public abstract Class getEntityClass();
}
