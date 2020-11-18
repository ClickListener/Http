package com.newtv.http.interceptor;

import com.newtv.http.chain.RealInterceptorChain;
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
    public void intercept(Chain chain, HttpListener listener) throws IOException {

        RealInterceptorChain realChain = (RealInterceptorChain) chain;

        BaseHttpRequest request = realChain.request();
        HttpService httpService = HttpServiceFactory.createHttpService();

        httpService.sendRequest(request, listener, realChain.eventListener());


    }
}
