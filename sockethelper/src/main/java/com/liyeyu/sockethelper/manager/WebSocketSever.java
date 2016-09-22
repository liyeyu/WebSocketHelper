package com.liyeyu.sockethelper.manager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.liyeyu.sockethelper.LogUtil;
import com.liyeyu.sockethelper.bean.SocketParams;

import static com.liyeyu.sockethelper.manager.WebSocketManager.RECEIVE_MESSAGE_ACTION;
import static com.liyeyu.sockethelper.manager.WebSocketManager.RECEIVE_MESSAGE_TAG;

/**
 * socket Service
 * Created by Liyeyu on 2016/5/27.
 */
public class WebSocketSever extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SocketParams tag = (SocketParams) intent.getSerializableExtra("params");
        startSocket(tag);
        return START_STICKY;
    }

    private void startSocket(SocketParams params) {

        if(WebSocketManager.getInstance().mConnectCallback!=null
                && WebSocketManager.getInstance().mConnectCallback.onPreConnect()){
            WebSocketManager.closeSocket();
            return;
        }
        if(WebSocketManager.mState==WebSocketManager.SocketState.CONNECT){
            if(WebSocketManager.getInstance().mConnectCallback!=null){
                WebSocketManager.getInstance().mConnectCallback.onConnect();
            }
            return;
        }
        WebSocketManager.mState = WebSocketManager.SocketState.CONNECTING;
        if(WebSocketManager.getInstance().mConnectCallback!=null){
            WebSocketManager.getInstance().mConnectCallback.onConnecting();
        }
        if(params!=null && TextUtils.isEmpty(params.url)){
            WebSocketManager.getInstance().connect(params, new WebSocketClientImpl.OnConnectHandler() {
                @Override
                public void onOpen() {
                    WebSocketManager.staySocketHeart();
                }
                @Override
                public void onMessage(String message) {
                    LogUtil.i("socket received: " + message);
                    Intent intent = new Intent(RECEIVE_MESSAGE_ACTION);
                    intent.putExtra(RECEIVE_MESSAGE_TAG, message);
                    sendBroadcast(intent);
                    if(WebSocketManager.getInstance().mConnectCallback!=null){
                        WebSocketManager.getInstance().mConnectCallback.onReceiver(message);
                    }
                }
                @Override
                public void onClose(String error) {
                    LogUtil.e("socket error:" + error);
                    WebSocketManager.closeSocket();
                }
            });
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        WebSocketManager.closeSocket();
    }

}
