package com.newtv.http.request;

import android.content.Context;


import com.newtv.http.HttpConfig;
import com.newtv.http.MethodType;
import com.newtv.http.retrofit.RetryWithDelay;

import java.io.Serializable;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

/**
 * @author ZhangXu
 * @date 2020/9/28
 */
public abstract class BaseHttpRequest implements Serializable {


    private Context mContext;

    private RetryWithDelay retryWithDelay;

    /**
     * 设置重试
     * @param retryWithDelay
     */
    public void setRetryWithDelay(RetryWithDelay retryWithDelay) {
        this.retryWithDelay = retryWithDelay;
    }

    public RetryWithDelay getRetryWithDelay() {
        return retryWithDelay;
    }

    public BaseHttpRequest(Context context) {
        this.mContext = context;
    }

    /**
     * 获得当前Request的标识
     * @return context
     */
    public Object getTag() {
        return mContext;
    }


    /**
     * 获得Retrofit的Observable
     * @param retrofit
     * @return
     */
    public abstract Observable<ResponseBody> getObservable(Retrofit retrofit);

    public abstract String getBaseUrl();

    public abstract HttpConfig getHttpConfig();

    public abstract @MethodType.Method
    int getMethodType();

}
