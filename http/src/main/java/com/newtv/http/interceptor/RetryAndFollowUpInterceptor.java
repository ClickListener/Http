package com.newtv.http.interceptor;

import com.newtv.http.EventListener;
import com.newtv.http.factory.HttpServiceFactory;
import com.newtv.http.internal.HttpListener;
import com.newtv.http.internal.HttpService;
import com.newtv.http.request.BaseHttpRequest;

import java.io.IOException;

/**
 * @author ZhangXu
 * @date 2020/11/16
 */
public class RetryAndFollowUpInterceptor implements NewInterceptor {

    private boolean canceled = false;

    @Override
    public void intercept(Chain chain, HttpListener listener, EventListener eventListener) throws IOException {
        // 如果请求还没发送，则直接返回
        if (canceled) {
            listener.onRequestError(new IOException("Canceled"));
            return;
        }
        BaseHttpRequest request = chain.request();
        chain.proceed(request, listener, eventListener);
    }


    public void cancel(BaseHttpRequest request) {
        this.canceled = true;
        HttpService httpService = HttpServiceFactory.createHttpService();
        if (httpService != null) httpService.cancelRequest(request.tag());
    }
}
