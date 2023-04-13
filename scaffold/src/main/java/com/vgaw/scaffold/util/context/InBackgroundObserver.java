package com.vgaw.scaffold.util.context;

import androidx.lifecycle.Observer;

import java.util.HashSet;
import java.util.Set;

public class InBackgroundObserver {
    private static InBackgroundObserver sInstance = new InBackgroundObserver();
    private Set<Observer<Boolean>> mObserverSet = new HashSet<>();

    public static InBackgroundObserver getInstance() {
        return sInstance;
    }

    private InBackgroundObserver() {}

    public void notify(boolean inBackground) {
        for (Observer<Boolean> item : mObserverSet) {
            item.onChanged(inBackground);
        }
    }

    public void registerObserver(Observer<Boolean> observer, boolean register) {
        if (register) {
            mObserverSet.add(observer);
        } else {
            mObserverSet.remove(observer);
        }
    }
}
