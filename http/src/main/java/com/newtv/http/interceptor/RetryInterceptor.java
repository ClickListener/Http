package com.newtv.http.interceptor;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.newtv.http.EventListener;
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
    public void intercept(Chain chain, HttpListener listener, EventListener eventListener) throws IOException {

        retryNum++;
        retry(chain, listener, eventListener);

    }


    private void retry(Chain chain, HttpListener listener, EventListener eventListener) throws IOException {

        Log.e("zhangxu", "retryNum = " + retryNum);

        BaseHttpRequest request = chain.request();

        HttpListener _listener = new HttpListener() {
            @Override
            public void onRequestResult(String result) {
                listener.onRequestResult(result);
            }

            @Override
            public void onRequestError(Throwable e) {
                // 如果是用户主动取消， 则不再进行重试
                if (e.toString().contains("Canceled") || e.toString().contains("Socket closed")) {
                    listener.onRequestError(e);
                    return;
                }
                if (retryNum < maxRetry) {
                    retryNum++;
                    mHandler.postDelayed(() -> {
                        try {
                            retry(chain, listener, eventListener);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }, delay);

                } else {
                    listener.onRequestError(e);
                }
            }
        };

        EventListener _eventListener = new EventListener() {
            @Override
            public void callStart() {
                if (retryNum == 1) eventListener.callStart();

            }

            @Override
            public void callEnd() {
                if (retryNum >= maxRetry) eventListener.callEnd();
            }

            @Override
            public void callFailed() {
                if (retryNum >= maxRetry) eventListener.callFailed();
            }
        };

        chain.proceed(request, _listener, _eventListener);


    }
}
