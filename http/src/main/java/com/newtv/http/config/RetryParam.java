package com.newtv.http.config;

import java.util.concurrent.TimeUnit;

/**
 * @author ZhangXu
 * @date 2020/11/9
 */
public class RetryParam {

    private final int maxRetryCount;
    private final int retryDelay;


    public RetryParam(int maxRetryCount, int retryDelay) {
        this.maxRetryCount = maxRetryCount;
        this.retryDelay = retryDelay;
    }

    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    public int getRetryDelay() {
        return retryDelay;
    }

}
