package com.liyeyu.sockethelper;

import android.app.Application;

import com.liyeyu.sockethelper.manager.WebSocketClientImpl;
import com.liyeyu.sockethelper.manager.WebSocketManager;

/**
 * Created by Liyeyu on 2016/9/22.
 */

public class SocketHelper {
    public static Application app;
    public static void init(Application context) {
        app = context;
        WebSocketManager.init(context);
    }

    public static void isDebug(boolean isDebug){
        LogUtil.DEBUG = isDebug;
    }

    public static void setTimeOut(int seconds){
        WebSocketClientImpl.SOCKET_TIME_OUT = seconds;
    }

    public static void setHeartInterval(int interval){
        WebSocketManager.SOCKET_HEART_INTERVAL= interval;
    }
}
