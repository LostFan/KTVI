package org.lostfan.ktv.model.entity;

import org.lostfan.ktv.model.EntityField;
import org.lostfan.ktv.utils.Observable;

import java.util.List;

public interface BaseModel extends Observable {

    String getEntityNameKey();

    List<EntityField> getFields();
}
