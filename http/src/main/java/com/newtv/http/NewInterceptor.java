package com.newtv.http;

import com.newtv.http.request.BaseHttpRequest;
import com.newtv.http.response.BaseResponse;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络拦截器， 可以对Request和Response做定制化操作
 * @author ZhangXu
 * @date 2020/11/9
 */
public abstract class NewInterceptor implements Interceptor {


    interface NewChain {
        BaseHttpRequest request();

        BaseResponse proceed(BaseHttpRequest request) throws IOException;
    }

    abstract BaseResponse internalIntercept(NewChain chain);


    @Override
    public Response intercept(Chain chain) throws IOException {


        Request request = chain.request();


        return null;
    }
}
