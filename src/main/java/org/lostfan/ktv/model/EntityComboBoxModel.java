package org.lostfan.ktv.model;

import java.util.List;
import java.util.Map;

import org.lostfan.ktv.utils.Observable;

/**
 * Created by Ihar_Niakhlebau on 14-Oct-15.
 */
public abstract class EntityComboBoxModel<T> extends Observable {

    public abstract  List<T> getList();

    public abstract List<T> getListByBeginningPartOfName(String str);

    public abstract  List<EntityField<T, ?>> getFields();

    public abstract Class getEntityClass();
}
