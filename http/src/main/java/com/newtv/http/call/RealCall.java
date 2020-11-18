package com.newtv.http.call;

import com.newtv.http.NewHttpClient;
import com.newtv.http.chain.RealInterceptorChain;
import com.newtv.http.interceptor.CallServerInterceptor;
import com.newtv.http.interceptor.NewInterceptor;
import com.newtv.http.interceptor.RetryAndFollowUpInterceptor;
import com.newtv.http.internal.HttpListener;
import com.newtv.http.request.BaseHttpRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



/**
 * @author ZhangXu
 * @date 2020/11/16
 */
public class RealCall implements Call{


    private final RetryAndFollowUpInterceptor retryAndFollowUpInterceptor;
    private final BaseHttpRequest request;

    private final NewHttpClient client;

    public static RealCall newRealCall(NewHttpClient client, BaseHttpRequest originalRequest) {
        return new RealCall(client, originalRequest);
    }


    private RealCall(NewHttpClient client, BaseHttpRequest request) {
        this.retryAndFollowUpInterceptor = new RetryAndFollowUpInterceptor();
        this.request = request;
        this.client = client;
    }

    @Override
    public void enqueue(HttpListener listener) {

        List<NewInterceptor> interceptors = new ArrayList<>();
        interceptors.addAll(client.interceptors());
        interceptors.add(retryAndFollowUpInterceptor);
        interceptors.add(new CallServerInterceptor());

        RealInterceptorChain chain = new RealInterceptorChain(request, interceptors, client.eventListener(),0);

        try {
            chain.proceed(request, listener);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void cancel() {
        retryAndFollowUpInterceptor.cancel(request);
    }
}