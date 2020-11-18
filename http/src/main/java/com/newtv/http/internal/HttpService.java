package com.newtv.http.internal;

import androidx.annotation.Nullable;

import com.newtv.http.EventListener;
import com.newtv.http.request.BaseHttpRequest;

/**
 * @author ZhangXu
 * @date 2020/9/28
 */
public interface HttpService {

    /**
     * 发送请求
     * @param request 请求
     * @param listener 回调
     * @param eventListener 发送事件的回调
     */
    void sendRequest(BaseHttpRequest request, HttpListener listener, EventListener eventListener);

    /**
     * 取消请求
     * @param tag 需要取消请求的TAG （一般为Context）
     */
    void cancelRequest(Object tag);

    /**
     * 取消所有请求
     */
    void cancelAllRequest();
}
