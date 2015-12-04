package org.lostfan.ktv.model.transform;

import java.util.List;
import java.util.Map;

import org.lostfan.ktv.domain.Entity;

public interface EntityTransformer<E extends Entity, D extends Entity> {

    D transformTo(E entity);
}
