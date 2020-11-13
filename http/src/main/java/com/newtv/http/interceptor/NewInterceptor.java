package com.newtv.http.interceptor;


import com.newtv.http.HttpListener;
import com.newtv.http.request.BaseHttpRequest;

import java.io.IOException;

/**
 * @author ZhangXu
 * @date 2020/11/10
 */
public interface NewInterceptor {
    void intercept(Chain chain, HttpListener listener) throws IOException;

    interface Chain {

        BaseHttpRequest request();

        Response proceed(BaseHttpRequest request);
    }

}
