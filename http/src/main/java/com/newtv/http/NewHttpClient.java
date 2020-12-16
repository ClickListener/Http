package com.newtv.http;


import com.newtv.http.call.Call;
import com.newtv.http.call.RealCall;
import com.newtv.http.interceptor.NewInterceptor;
import com.newtv.http.request.BaseHttpRequest;

import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;



/**
 * @author ZhangXu
 * @date 2020/11/16
 */
public class NewHttpClient implements Call.Factory{

    private final List<NewInterceptor> interceptors;

    private final EventListener eventListener;


    private NewHttpClient() {
        this(new Builder());
    }


    private NewHttpClient(Builder builder) {
        this.interceptors = builder.interceptors;
        this.eventListener = builder.eventListener;
    }

    public List<NewInterceptor> interceptors() {
        return interceptors;
    }


    public EventListener eventListener() {
        return eventListener;
    }


    @Override
    public Call newCall(BaseHttpRequest request) {
        return RealCall.newRealCall(this, request);
    }

    public static class Builder {

        private final List<NewInterceptor> interceptors = new ArrayList<>();

        private EventListener eventListener = EventListener.NONE;

        public Builder addInterceptors(List<NewInterceptor> interceptors) {
            if (interceptors == null) throw new IllegalArgumentException("interceptor == null");
            this.interceptors.addAll(interceptors);
            return this;
        }

        public Builder eventListener(EventListener eventListener) {
            if (eventListener != null) {
                this.eventListener = eventListener;
            }
            return this;
        }

        public NewHttpClient build() {
            return new NewHttpClient(this);
        }

    }

}
