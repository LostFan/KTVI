package org.lostfan.ktv.utils;

import java.util.*;

public abstract class BaseObservable implements Observable {

    private Set<Observer> observers;

    public BaseObservable() {
        this.observers = new HashSet<>();
    }

    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    protected void notifyObservers(Object args) {
        for (Observer observer : this.observers) {
            observer.update(args);
        }
    }
}
