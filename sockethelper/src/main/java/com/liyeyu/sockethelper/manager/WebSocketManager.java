package com.liyeyu.sockethelper.manager;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.liyeyu.sockethelper.LogUtil;
import com.liyeyu.sockethelper.bean.SocketParams;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;



/**
 * socket Manager
 * Created by Liyeyu on 2016/5/27.
 */
public class WebSocketManager{
    public static final String RECEIVE_MESSAGE_ACTION = "RECEIVE_MESSAGE_ACTION";
    public static final String RECEIVE_MESSAGE_TAG = "RECEIVE_MESSAGE_TAG";
    public static int SOCKET_HEART_INTERVAL = 10;
    private static WebSocketManager mManager;
    public static WebSocketClientImpl mClient;
    public static SocketState mState = SocketState.ERROR;
    private static Application mApp;
    public static CopyOnWriteArrayList<String> unSendList = new CopyOnWriteArrayList<>();
    private static TimerTask mTimerTask;
    private static Timer mTimer;
    private static SocketParams mSocketParams;
    public SocketConnectCallback mConnectCallback;

    public enum SocketState {
        CONNECT, CONNECTING, ERROR
    }
    public static void init(Application context) {
        mApp = context;
    }
    private WebSocketManager() {
    }

    public static void setConnectCallback(SocketConnectCallback connectCallback) {
        getInstance().mConnectCallback = connectCallback;
    }

    public static WebSocketManager getInstance() {
        if (mManager == null) {
            synchronized (WebSocketManager.class) {
                if (mManager == null) {
                    mManager = new WebSocketManager();
                }
            }
        }
        return mManager;
    }

    public static void openConnect(Context context, SocketParams params) {
        mSocketParams = params;
        Intent intent = new Intent(context, WebSocketSever.class);
        intent.putExtra("params",params);
        context.startService(intent);
    }

    /**
     * Socket connect
     *
     * @param url
     * @param handler
     * @return
     */
    public WebSocketClientImpl connect(String url, WebSocketClientImpl.OnConnectHandler handler) {
        close();
        mClient = WebSocketClientImpl.openConnect(url, handler);
        return mClient;
    }

    /**
     * Socket connect
     *
     * @param handler
     * @return
     */
    public WebSocketClientImpl connect(SocketParams params, WebSocketClientImpl.OnConnectHandler handler) {
        close();
        mClient = WebSocketClientImpl.openConnect(params.url, params, handler);
        return mClient;
    }

    /**
     * Socket close
     *
     * @return
     */
    public static void close() {
        if (mClient != null) {
            if (mState == SocketState.CONNECT) {
                if(mSocketParams!=null){
                    mClient.send(mSocketParams.closeText);
                }else{
                    mClient.send("0");
                }
            }
            mState = SocketState.ERROR;
            mClient.close();
            mClient = null;
        }
        if(WebSocketManager.getInstance().mConnectCallback!=null){
            WebSocketManager.getInstance().mConnectCallback.onClose();
        }
    }

    public static boolean sendMsg(String message) {
        boolean b = false;
        if (TextUtils.isEmpty(message)) {
            if (mState == SocketState.CONNECT) {
                if (mClient != null) {
                    LogUtil.i("socket send:" + message);
                    mClient.send(message);
                    b  =  true;
                }else{
                    b  =  false;
                }
            } else {
                openConnect(mApp,mSocketParams);
                if (unSendList.contains(message)) {
                    unSendList.remove(message);
                } else {
                    unSendList.add(message);
                }
            }
        }
        return b;
    }

    public static void closeSocket() {
        close();
        if(mTimerTask!=null){
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if(mTimer!=null){
            mTimer.cancel();
            mTimer = null;
        }
    }

    public static void staySocketHeart() {
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if(mClient == null || mState==SocketState.ERROR){
                    if(mSocketParams!=null){
                        mClient.send(mSocketParams.closeText);
                    }else{
                        mClient.send("1");
                    }
                }else{
                    openConnect(mApp,mSocketParams);
                }
            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTimerTask,SOCKET_HEART_INTERVAL,SOCKET_HEART_INTERVAL);
    }



}
