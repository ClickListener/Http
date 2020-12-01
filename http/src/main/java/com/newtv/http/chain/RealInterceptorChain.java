package com.newtv.http.chain;

import com.newtv.http.EventListener;
import com.newtv.http.internal.HttpListener;
import com.newtv.http.interceptor.NewInterceptor;
import com.newtv.http.request.BaseHttpRequest;

import java.io.IOException;
import java.util.List;

/**
 * @author ZhangXu
 * @date 2020/11/13
 */
public class RealInterceptorChain implements NewInterceptor.Chain {


    private final BaseHttpRequest request;
    private final List<NewInterceptor> interceptors;

    private final int index;


    public RealInterceptorChain(BaseHttpRequest request, List<NewInterceptor> interceptors, int index) {
        this.request = request;
        this.interceptors = interceptors;
        this.index = index;
    }

    @Override
    public BaseHttpRequest request() {
        return request;
    }

    @Override
    public void proceed(BaseHttpRequest request, HttpListener listener, EventListener eventListener) throws IOException{

        if (index >= interceptors.size()) {
            throw new AssertionError();
        }

        RealInterceptorChain chain = new RealInterceptorChain(request, interceptors, index + 1);

        NewInterceptor interceptor = interceptors.get(index);

        interceptor.intercept(chain, listener, eventListener);


    }
}
