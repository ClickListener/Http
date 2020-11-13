package com.newtv.http.okhttp;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author ZhangXu
 * @date 2020/11/10
 */
public class RetryInterceptor implements Interceptor {

    private final int maxRetry;
    private int retryNum = 0;


    public RetryInterceptor(int maxRetry) {
        this.maxRetry = maxRetry;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        while(!response.isSuccessful() && retryNum < maxRetry) {
            Log.e("okhttp", "retryNum = " + retryNum);
            retryNum++;
            response = chain.proceed(request);
        }
        return response;
    }
}
