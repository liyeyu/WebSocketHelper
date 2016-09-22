package com.liyeyu.sockethelper.manager;

/**
 * Created by Liyeyu on 2016/9/22.
 */

public interface SocketConnectCallback {
    boolean onPreConnect();
    void onConnecting();
    void onConnect();
    void onClose();
    void onReceiver(String message);
}
