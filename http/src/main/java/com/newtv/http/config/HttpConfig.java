package com.newtv.http.config;



import com.newtv.http.NewInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 *
 * 网络设置
 * @author ZhangXu
 * @date 2020/10/20
 */
public class HttpConfig {


    /**
     * 连接超时时间
     */
    private int connectTimeout;

    /**
     * 连接超时时间 单位
     */
    private TimeUnit connectTimeoutTimeUnit;

    /**
     * 读超时时间
     */
    private int readTimeout;


    /**
     * 读超时时间 单位
     */
    private TimeUnit readTimeoutTimeUnit;


    /**
     * 写 超时时间
     */
    private int writeTimeout;


    /**
     * 写 超时时间 单位
     */
    private TimeUnit writeTimeoutTimeUnit;


    /**
     * 拦截器
     */
    private List<NewInterceptor> interceptors;


    private RetryParam retryParam;

    public HttpConfig(Builder builder) {

        this.connectTimeout = builder.connectTimeout;
        this.connectTimeoutTimeUnit = builder.connectTimeoutTimeUnit;
        this.readTimeout = builder.readTimeout;
        this.readTimeoutTimeUnit = builder.readTimeoutTimeUnit;
        this.writeTimeout = builder.writeTimeout;
        this.writeTimeoutTimeUnit = builder.writeTimeoutTimeUnit;
        this.interceptors = builder.interceptors;
        this.retryParam = builder.retryParam;
    }


    public int getConnectTimeout() {
        return connectTimeout;
    }

    public TimeUnit getConnectTimeoutTimeUnit() {
        return connectTimeoutTimeUnit;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public TimeUnit getReadTimeoutTimeUnit() {
        return readTimeoutTimeUnit;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public TimeUnit getWriteTimeoutTimeUnit() {
        return writeTimeoutTimeUnit;
    }

    public List<NewInterceptor> getInterceptors() {
        return interceptors;
    }

    public RetryParam getRetryParam() {
        return retryParam;
    }


    public static final class Builder {

        /**
         * 连接超时时间
         */
        private int connectTimeout;

        /**
         * 连接超时时间 单位
         */
        private TimeUnit connectTimeoutTimeUnit;

        /**
         * 读超时时间
         */
        private int readTimeout;


        /**
         * 读超时时间 单位
         */
        private TimeUnit readTimeoutTimeUnit;


        /**
         * 写 超时时间
         */
        private int writeTimeout;


        /**
         * 写 超时时间 单位
         */
        private TimeUnit writeTimeoutTimeUnit;


        /**
         * 拦截器
         */
        private List<NewInterceptor> interceptors = new ArrayList<>();

        private RetryParam retryParam;


        public Builder addInterceptor(NewInterceptor interceptor) {
            if (interceptor != null) {
                interceptors.add(interceptor);
            }
            return this;
        }

        public Builder connectTimeout(int timeout, TimeUnit TimeUnit) {
            if (timeout > 0) {
                this.connectTimeout = timeout;
                this.connectTimeoutTimeUnit = TimeUnit;
            }
            return this;
        }

        public Builder readTimeout(int timeout, TimeUnit TimeUnit) {
            if (timeout > 0) {
                this.readTimeout = timeout;
                this.readTimeoutTimeUnit = TimeUnit;
            }
            return this;
        }

        public Builder writeTimeout(int timeout, TimeUnit TimeUnit) {
            if (timeout > 0) {
                this.writeTimeout = timeout;
                this.writeTimeoutTimeUnit = TimeUnit;
            }
            return this;
        }

        public Builder retryParams(RetryParam retryParam) {
            if (retryParam != null) {
                this.retryParam = retryParam;
            }
            return this;
        }


        public HttpConfig build() {
            return new HttpConfig(this);
        }



    }





}
