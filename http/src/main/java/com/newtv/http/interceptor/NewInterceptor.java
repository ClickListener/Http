package com.newtv.http.interceptor;


import com.newtv.http.EventListener;
import com.newtv.http.internal.HttpListener;
import com.newtv.http.request.BaseHttpRequest;

import java.io.IOException;

/**
 * @author ZhangXu
 * @date 2020/11/10
 */
public interface NewInterceptor {
    void intercept(Chain chain, HttpListener listener, EventListener eventListener) throws IOException;

    interface Chain {

        BaseHttpRequest request();

        void proceed(BaseHttpRequest request, HttpListener listener, EventListener eventListener) throws IOException;
    }

}
