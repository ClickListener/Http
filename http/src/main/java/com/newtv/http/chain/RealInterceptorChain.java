package com.newtv.http.chain;

import com.newtv.http.interceptor.NewInterceptor;
import com.newtv.http.request.BaseHttpRequest;

import java.util.List;

/**
 * @author ZhangXu
 * @date 2020/11/13
 */
public class RealInterceptorChain implements NewInterceptor.Chain {


    private BaseHttpRequest request;
    private List<NewInterceptor> interceptors;
    @Override
    public BaseHttpRequest request() {
        return null;
    }

    @Override
    public Response proceed(BaseHttpRequest request) {
        return null;
    }
}
