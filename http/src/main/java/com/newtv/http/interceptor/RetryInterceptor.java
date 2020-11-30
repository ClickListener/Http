package com.newtv.http.interceptor;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.newtv.http.internal.HttpListener;
import com.newtv.http.request.BaseHttpRequest;

import java.io.IOException;

/**
 * @author ZhangXu
 * @date 2020/11/18
 */
public class RetryInterceptor implements NewInterceptor {

    private final int maxRetry;
    private int retryNum = 0;
    private final int delay;

    private static final Handler mHandler = new Handler(Looper.getMainLooper());

    public RetryInterceptor(int maxRetry) {
        this(maxRetry, 0);
    }

    public RetryInterceptor(int maxRetry, int delay) {
        this.maxRetry = maxRetry;
        this.delay = delay;
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
                    mHandler.postDelayed(() -> {
                        try {
                            retry(chain, listener);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }, delay);

                } else {
                    listener.onRequestError(e);
                }
            }
        };
        chain.proceed(request, _listener);


    }
}
