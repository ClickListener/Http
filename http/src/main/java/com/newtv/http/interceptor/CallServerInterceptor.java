package com.newtv.http.interceptor;

import com.newtv.http.HttpListener;
import com.newtv.http.request.BaseHttpRequest;

import java.io.IOException;

/**
 * @author ZhangXu
 * @date 2020/11/13
 */
public class CallServerInterceptor implements NewInterceptor {


    @Override
    public void intercept(Chain chain, HttpListener listener) throws IOException {

        BaseHttpRequest request = chain.request();


        return null;
    }
}
