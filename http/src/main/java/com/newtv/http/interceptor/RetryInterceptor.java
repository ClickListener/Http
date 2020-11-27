package com.newtv.http.interceptor;

import android.util.Log;

import com.newtv.http.internal.HttpListener;
import com.newtv.http.request.BaseHttpRequest;

import java.io.IOException;

/**
 * @author ZhangXu
 * @date 2020/11/18
 */
public class RetryInterceptor implements NewInterceptor{

    private final int maxRetry;
    private int retryNum = 0;

    public RetryInterceptor(int maxRetry) {
        this.maxRetry = maxRetry;
    }

    @Override
    public void intercept(Chain chain, HttpListener listener) throws IOException {

        retry(chain, listener);

    }


    private void retry(Chain chain, HttpListener listener) throws IOException {
        BaseHttpRequest request = chain.request();

        HttpListener _listener = new HttpListener() {
            @Override
            public void onRequestResult(String result) {
                listener.onRequestResult(result);
            }

            @Override
            public void onRequestError(Throwable e) {
                if (retryNum < maxRetry) {
                    Log.e("zhangxu", "retryNum = " + retryNum);
                    retryNum++;
                    try {
                        retry(chain, listener);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                } else {
                    listener.onRequestError(e);
                }
            }
        };
        chain.proceed(request, _listener);


    }
}
