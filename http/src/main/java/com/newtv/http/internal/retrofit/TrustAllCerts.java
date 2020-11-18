package com.newtv.http.internal.retrofit;

import android.annotation.SuppressLint;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @author weihaichao
 * @date 2020/9/22 09:14
 */
public class TrustAllCerts implements X509TrustManager {
    @SuppressLint("TrustAllX509TrustManager")
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    @SuppressLint("TrustAllX509TrustManager")
    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    public static class TrustAllParams {
        public SSLSocketFactory sSLSocketFactory;
        public X509TrustManager trustManager;
    }

    public static TrustAllParams createSSLSocketFactory() {
        TrustAllParams params = new TrustAllParams();
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            X509TrustManager manager = new TrustAllCerts();
            context.init(null, new TrustManager[]{manager} , new SecureRandom());
            params.sSLSocketFactory = context.getSocketFactory();
            params.trustManager = manager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

    public static class TrustAllHostnameVerifier implements HostnameVerifier {

        @SuppressLint("BadHostnameVerifier")
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
