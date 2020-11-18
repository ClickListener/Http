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

    private final SocketFactory socketFactory;
    private final SSLSocketFactory sslSocketFactory;

    private final EventListener eventListener;


    private NewHttpClient() {
        this(new Builder());
    }


    private NewHttpClient(Builder builder) {
        this.interceptors = builder.interceptors;
        this.socketFactory = builder.socketFactory;
        this.eventListener = builder.eventListener;

        if (builder.sslSocketFactory != null) {
            this.sslSocketFactory = builder.sslSocketFactory;
        } else {
            X509TrustManager trustManager = platformTrustManager();
            this.sslSocketFactory = newSslSocketFactory(trustManager);
        }
    }

    public List<NewInterceptor> interceptors() {
        return interceptors;
    }


    public EventListener eventListener() {
        return eventListener;
    }

    public X509TrustManager platformTrustManager() {
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }
            return (X509TrustManager) trustManagers[0];
        } catch (GeneralSecurityException e) {
            throw assertionError("No System TLS", e); // The system has no TLS. Just give up.
        }
    }

    private SSLSocketFactory newSslSocketFactory(X509TrustManager trustManager) {
        try {
            SSLContext sslContext = getSSLContext();
            sslContext.init(null, new TrustManager[] { trustManager }, null);
            return sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw assertionError("No System TLS", e); // The system has no TLS. Just give up.
        }
    }

    public AssertionError assertionError(String message, Exception e) {
        AssertionError assertionError = new AssertionError(message);
        try {
            assertionError.initCause(e);
        } catch (IllegalStateException ise) {
            // ignored, shouldn't happen
        }
        return assertionError;
    }

    public SSLContext getSSLContext() {
        String jvmVersion = System.getProperty("java.specification.version");
        if ("1.7".equals(jvmVersion)) {
            try {
                // JDK 1.7 (public version) only support > TLSv1 with named protocols
                return SSLContext.getInstance("TLSv1.2");
            } catch (NoSuchAlgorithmException e) {
                // fallback to TLS
            }
        }

        try {
            return SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No TLS provider", e);
        }
    }



    @Override
    public Call newCall(BaseHttpRequest request) {
        return RealCall.newRealCall(this, request);
    }

    public static class Builder {

        private final List<NewInterceptor> interceptors = new ArrayList<>();

        private SocketFactory socketFactory;
        private SSLSocketFactory sslSocketFactory;

        private EventListener eventListener = EventListener.NONE;


        public Builder socketFactory(SocketFactory socketFactory) {
            if (socketFactory == null) throw new NullPointerException("socketFactory == null");
            this.socketFactory = socketFactory;
            return this;
        }

        public Builder sslSocketFactory(SSLSocketFactory sslSocketFactory) {
            if (sslSocketFactory == null) throw new NullPointerException("sslSocketFactory == null");
            this.sslSocketFactory = sslSocketFactory;
            return this;
        }

        public Builder sslSocketFactory(
                SSLSocketFactory sslSocketFactory, X509TrustManager trustManager) {
            if (sslSocketFactory == null) throw new NullPointerException("sslSocketFactory == null");
            if (trustManager == null) throw new NullPointerException("trustManager == null");
            this.sslSocketFactory = sslSocketFactory;
            return this;
        }

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
