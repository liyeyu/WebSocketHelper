package com.liyeyu.websockethelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.liyeyu.sockethelper.SocketHelper;
import com.liyeyu.sockethelper.bean.SocketParams;
import com.liyeyu.sockethelper.manager.SocketConnectCallback;
import com.liyeyu.sockethelper.manager.WebSocketManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SocketHelper.init(getApplication());
        WebSocketManager.openConnect(this,new SocketParams("",null));
        WebSocketManager.setConnectCallback(new SocketConnectCallback() {
            @Override
            public boolean onPreConnect() {
                return false;
            }

            @Override
            public void onConnecting() {

            }

            @Override
            public void onConnect() {

            }

            @Override
            public void onClose() {

            }

            @Override
            public void onReceiver(String message) {

            }
        });
        WebSocketManager.sendMsg("133");
    }
}
