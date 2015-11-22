package org.lostfan.ktv.model.transform;

import org.lostfan.ktv.domain.Entity;

public interface EntityTransformer<E extends Entity, D extends Entity> {

    D transformTo(E entity);

    E transformFrom(D dto);
}
