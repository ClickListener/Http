package com.newtv.http.call;


import com.newtv.http.internal.HttpListener;
import com.newtv.http.request.BaseHttpRequest;

/**
 * @author ZhangXu
 * @date 2020/11/16
 */
public interface Call {

    void enqueue(HttpListener listener);

    void cancel();

    interface Factory {
        Call newCall(BaseHttpRequest request);
    }
}
