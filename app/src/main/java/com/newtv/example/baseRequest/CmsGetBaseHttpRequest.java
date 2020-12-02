package com.newtv.example.baseRequest;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.newtv.http.config.HttpConfig;
import com.newtv.http.config.RetryParam;
import com.newtv.http.TrustAllCerts;
import com.newtv.http.request.GetBaseHttpRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ZhangXu
 * @date 2020/11/10
 */
public abstract class CmsGetBaseHttpRequest extends GetBaseHttpRequest {

    public CmsGetBaseHttpRequest(Context context) {
        super(context);
    }

    @Override
    public Map<String, String> headers() {
        Map<String, String> header = new HashMap<>();
        header.put("host_type", "SEARCH");
        return header;
    }

    @Override
    public String baseUrl() {
        return "http://118.89.223.215/";
    }

    @Override
    public String toJson() {
        return JSONObject.toJSONString(this);
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
}
