package org.lostfan.ktv.utils;

import java.util.HashSet;
import java.util.Set;

public abstract class Observable {

    private Set<Observer> observers;

    public Observable() {
        this.observers = new HashSet<>();
    }

    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    protected void notifyObservers(Object args) {
        for (Observer observer : this.observers) {
            observer.update(args);
        }
    }
}
