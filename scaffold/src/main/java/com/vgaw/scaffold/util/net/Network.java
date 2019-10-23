package com.vgaw.scaffold.util.net;

public class Network {
    private static Network sNetwork;

    private boolean wifi;
    private boolean mobile;
    private boolean connected;

    public static Network getInstance() {
        if (sNetwork == null) {
            synchronized (Network.class) {
                if (sNetwork == null) {
                    sNetwork = new Network();
                }
            }
        }
        return sNetwork;
    }


    private Network() {}

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isMobile() {
        return mobile;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }

}
