package com.liyeyu.sockethelper.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Liyeyu on 2016/9/22.
 */

public class SocketParams implements Serializable {
    public String url;
    public boolean isAddParamsToUrl = false;
    public String connectText = "1";
    public String closeText = "0";
    public Map<String,String> params;

    public SocketParams(String url, Map<String, String> params) {
        this.url = url;
        this.params = params;
    }
    public SocketParams(Map<String, String> params, String closeText, String connectText, String url) {
        this.params = params;
        this.closeText = closeText;
        this.connectText = connectText;
        this.url = url;
    }
    public SocketParams(String url) {
        this.url = url;
    }
}
