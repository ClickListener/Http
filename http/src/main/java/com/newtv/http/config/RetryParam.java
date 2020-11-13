package com.newtv.http.config;

import java.util.concurrent.TimeUnit;

/**
 * @author ZhangXu
 * @date 2020/11/9
 */
public class RetryParam {

    private final int maxRetryCount;
    private final int retryDelay;
    private final TimeUnit timeUnit;


    public RetryParam(int maxRetryCount, int retryDelay, TimeUnit timeUnit) {
        this.maxRetryCount = maxRetryCount;
        this.retryDelay = retryDelay;
        this.timeUnit = timeUnit;
    }


    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    public int getRetryDelay() {
        return retryDelay;
    }


    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
}
