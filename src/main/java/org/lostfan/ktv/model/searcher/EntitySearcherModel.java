package org.lostfan.ktv.model.searcher;

import java.util.List;

import org.lostfan.ktv.dao.EntityDAO;
import org.lostfan.ktv.domain.Entity;
import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.utils.BaseObservable;

public abstract class EntitySearcherModel<T extends Entity> extends BaseObservable {

    private List<T> entities;

    protected abstract EntityDAO<T> getDao();

    public List<T> getList() {
        if (this.entities == null) {
            this.entities = getDao().getAll();
        }

        return this.entities;
    }

    protected void setList(List<T> entities) {
        this.entities = entities;
        this.notifyObservers(null);
    }

    public void setSearchQuery(String query) {
        setList(getDao().getAllContainsInName(query));
    }

    public T getEntity(int id) {

        return getDao().get(id);
    }

    public abstract Class getEntityClass();

    public abstract String getEntityNameKey();

    public abstract List<EntityField> getFields();
}
