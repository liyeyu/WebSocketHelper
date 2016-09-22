package com.liyeyu.sockethelper.manager;


import com.liyeyu.sockethelper.LogUtil;
import com.liyeyu.sockethelper.bean.SocketParams;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * socket client
 * Created by Liyeyu on 2016/5/27.
 */
public class WebSocketClientImpl extends WebSocketClient {
    public static int SOCKET_TIME_OUT = 10;

    private static OnConnectHandler mHandler;
    private static WebSocketClientImpl client;

    private WebSocketClientImpl(URI serverURI) {
        super(serverURI);
    }

    private WebSocketClientImpl(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    private WebSocketClientImpl(URI serverUri, Draft draft, Map<String, String> headers, int timeout) {
        super(serverUri, draft, headers, timeout);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        LogUtil.i("socket opened connect....");
        WebSocketManager.mState = WebSocketManager.SocketState.CONNECT;
        if(mHandler!=null){
            mHandler.onOpen();
        }
    }

    @Override
    public void onMessage(String message) {
        LogUtil.i("socket received: " + message);
        if(mHandler!=null){
            mHandler.onMessage(message);
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        LogUtil.d("socket close: " + s);
        WebSocketManager.mState = WebSocketManager.SocketState.ERROR;
        if(mHandler!=null){
            mHandler.onClose(s);
        }
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        WebSocketManager.mState = WebSocketManager.SocketState.ERROR;
    }

    @Override
    public void connect() {
        WebSocketManager.mState = WebSocketManager.SocketState.CONNECTING;
        super.connect();
    }

    /**
     * open Connect
     * @param url
     * @param handler
     * @return
     */
    public static WebSocketClientImpl openConnect(String url, OnConnectHandler handler){
        mHandler = handler;
        try {
            Draft_17 draft_17 = new Draft_17();
            client = new WebSocketClientImpl(new URI(url),draft_17);
            client.connect();
          return  client;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            client.close();
        }
        return null;
    }
    /**
     * open Connect
     * @param url
     * @param handler
     * @return
     */
    public static WebSocketClientImpl openConnect(String url, SocketParams socketParams, OnConnectHandler handler){
        mHandler = handler;
        try {
            Draft_17 draft_17 = new Draft_17();
            if(socketParams.isAddParamsToUrl && socketParams.params!=null){
                url = url+"?";
                for (Map.Entry<String,String> entry:socketParams.params.entrySet()) {
                    url += entry.getKey()+"="+entry.getValue()+"&";
                }
                url = url.substring(0,url.length()-1);
                LogUtil.i(WebSocketManager.mState.name() + " " + url);
                client = new WebSocketClientImpl(new URI(url),draft_17,socketParams.params, SOCKET_TIME_OUT);
            }else if(socketParams.params!=null){
                client = new WebSocketClientImpl(new URI(url),draft_17,socketParams.params, SOCKET_TIME_OUT);
            }else{
                client = new WebSocketClientImpl(new URI(url),draft_17);
            }
            client.connect();
          return  client;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            client.close();
        }
        return null;
    }

    public interface OnConnectHandler{
         void onOpen();
         void onMessage(String message);
         void onClose(String error);
    }

}
