package org.lostfan.ktv.utils;

public interface Observable {

    void addObserver(Observer observer);

    void removeObserver(Observer observer);
}
