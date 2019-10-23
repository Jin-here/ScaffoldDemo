package com.vgaw.scaffold.util.net;

import java.util.Observable;

public class NetworkChange extends Observable {
    private static NetworkChange sInstance = null;

    public static NetworkChange getInstance() {
        if (null == sInstance) {
            sInstance = new NetworkChange();
        }
        return sInstance;
    }
    //通知观察者数据改变

    public void notifyDataChange(Network net) {
        setChanged();
        notifyObservers(net);
    }

}

