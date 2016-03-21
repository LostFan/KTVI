package org.lostfan.ktv.dao;

public interface Transactional {

    void transactionBegin();

    void commit();

    void rollback();
}
