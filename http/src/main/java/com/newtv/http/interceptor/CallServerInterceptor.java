package com.newtv.http.interceptor;

import com.newtv.http.EventListener;
import com.newtv.http.internal.HttpListener;
import com.newtv.http.internal.HttpService;
import com.newtv.http.factory.HttpServiceFactory;
import com.newtv.http.request.BaseHttpRequest;

import java.io.IOException;


/**
 * @author ZhangXu
 * @date 2020/11/13
 */
public class CallServerInterceptor implements NewInterceptor {


    @Override
    public void intercept(Chain chain, HttpListener listener, EventListener eventListener) throws IOException {

        BaseHttpRequest request = chain.request();
        HttpService httpService = HttpServiceFactory.createHttpService();
        httpService.sendRequest(request, listener, eventListener);


    }
}
