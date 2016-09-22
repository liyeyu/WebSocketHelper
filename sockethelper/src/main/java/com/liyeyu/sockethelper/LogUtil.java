package com.liyeyu.sockethelper;

import android.util.Log;


public class LogUtil {
    public static boolean DEBUG = false;
    public static final String TAG = "socket_helper";

    public static void v(String msg) {
        if(DEBUG){
            Log.v(TAG, msg);
        }
    }

    public static void d(String msg) {
        if(DEBUG){
            Log.d(TAG, msg);
        }
    }

    public static void i(String msg) {
        if(DEBUG){
            Log.i(TAG, msg);
        }
    }

    public static void w(String msg) {
        if(DEBUG){
            Log.w(TAG, msg);
        }
    }

    public static void e(String msg) {
        if(DEBUG){
            Log.e(TAG, msg);
        }
    }
}
