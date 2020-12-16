package com.newtv.http.config;

import java.util.concurrent.TimeUnit;

/**
 * @author ZhangXu
 * @date 2020/11/9
 */
public class RetryParam {

    /**
     * 重试次数
     */
    private final int maxRetryCount;
    /**
     * 重试时间间隔
     */
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
