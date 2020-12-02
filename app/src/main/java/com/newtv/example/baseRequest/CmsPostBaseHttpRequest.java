package com.newtv.example.baseRequest;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.newtv.http.TrustAllCerts;
import com.newtv.http.config.HttpConfig;
import com.newtv.http.config.RetryParam;
import com.newtv.http.request.PostBaseHttpRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ZhangXu
 * @date 2020/12/1
 */
public abstract class CmsPostBaseHttpRequest extends PostBaseHttpRequest {

    public CmsPostBaseHttpRequest(Context context) {
        super(context);
    }

    @Override
    public HttpConfig httpConfig() {
        HttpConfig.Builder builder = new HttpConfig.Builder();
        RetryParam retryParam = new RetryParam(3, 1000);
        return builder.readTimeout(3, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .retryParams(retryParam)
                .sslSocketFactory(TrustAllCerts.createSSLSocketFactory().sSLSocketFactory, TrustAllCerts.createSSLSocketFactory().trustManager)
                .hostnameVerifier(new TrustAllCerts.TrustAllHostnameVerifier())
                .build();
    }

    @Override
    public String baseUrl() {
        return "http://118.89.223.215/";
    }

    @Override
    public Map<String, String> headers() {
        Map<String, String> header = new HashMap<>();
        header.put("host_type", "ACTIVATE");
        return header;
    }

    @Override
    public String toJson() {
        return JSONObject.toJSONString(this);
    }
}
