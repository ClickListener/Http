package com.newtv.http;

import com.newtv.http.request.BaseHttpRequest;
import com.newtv.http.response.BaseResponse;

import java.io.IOException;
import java.util.List;

/**
 * @author ZhangXu
 * @date 2020/11/9
 */
public class NewRealInterceptorChain implements NewInterceptor.NewChain {

    private final List<NewInterceptor> interceptors;
    private final int index;
    private final BaseHttpRequest request;


    public NewRealInterceptorChain(List<NewInterceptor> interceptors, BaseHttpRequest request, int index) {
        this.interceptors = interceptors;
        this.request = request;
        this.index = index;
    }

    @Override
    public BaseHttpRequest request() {
        return request;
    }

    @Override
    public BaseResponse proceed(BaseHttpRequest request) throws IOException {


        return null;
    }
}
