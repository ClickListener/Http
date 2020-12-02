package com.newtv.http.internal.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.newtv.http.EventListener;
import com.newtv.http.config.HttpConfig;
import com.newtv.http.request.BaseHttpRequest;
import com.newtv.http.internal.HttpListener;
import com.newtv.http.internal.HttpService;
import com.newtv.http.MethodType;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author ZhangXu
 * @date 2020/11/6
 */
public class OkHttpServiceImpl implements HttpService {


    private static volatile OkHttpServiceImpl INSTANCE;

    private OkHttpClient mClient;

    private final Map<Object, List<Call>> callMap = new ConcurrentHashMap<>();

    private final Handler handler = new Handler(Looper.getMainLooper());

    private OkHttpServiceImpl() {
    }

    public static OkHttpServiceImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (OkHttpServiceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new OkHttpServiceImpl();
                }
            }
        }
        return INSTANCE;

    }


    public OkHttpClient getOkHttpClient(BaseHttpRequest request, EventListener eventListener) {

        OkHttpClient.Builder builder;

        if (mClient == null) {
            builder = new OkHttpClient.Builder();
        } else {
            builder = mClient.newBuilder();
        }


        builder.eventListener(new okhttp3.EventListener() {
            @Override
            public void callStart(Call call) {
                eventListener.callStart();
            }

            @Override
            public void callEnd(Call call) {
                eventListener.callEnd();
            }

            @Override
            public void callFailed(Call call, IOException ioe) {
                eventListener.callFailed();
            }
        });

        HttpConfig config = request.httpConfig();
        if (config != null) {
            if (config.getHostnameVerifier() != null) {
                builder.hostnameVerifier(config.getHostnameVerifier());
            }

            if (config.getSslSocketFactory() != null && config.getX509TrustManager() != null) {
                builder.sslSocketFactory(config.getSslSocketFactory(), config.getX509TrustManager());
            }
            if (config.getConnectTimeout() > 0) {
                builder.connectTimeout(config.getConnectTimeout(), config.getConnectTimeoutTimeUnit());
            }
            if (config.getReadTimeout() > 0) {
                builder.readTimeout(config.getReadTimeout(), config.getReadTimeoutTimeUnit());
            }

            if (config.getWriteTimeout() > 0) {
                builder.writeTimeout(config.getWriteTimeout(), config.getWriteTimeoutTimeUnit());
            }
        }

        // 默认的一些设置
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logInterceptor);
        mClient = builder.build();
        return mClient;
    }

    @Override
    public void sendRequest(BaseHttpRequest request, HttpListener listener, EventListener eventListener) {
        Call call;
        List<Call> calls = callMap.get(request.tag());

        Request okHttpRequest;

        if (request.methodType() == MethodType.GET) {

            // 组装参数
            HttpUrl.Builder builder = HttpUrl.parse(request.baseUrl() + request.secondUrl())
                    .newBuilder();
            Map<String, String> params = request.params();
            if (params != null) {
                Set<String> keys = params.keySet();
                for (String key : keys) {
                    builder = builder.addEncodedQueryParameter(key, params.get(key));
                }
            }
            okHttpRequest = new Request.Builder()
                    .headers(convertHeader(request))
                    .url(builder.build())
                    .get()
                    .build();
        } else {
            MediaType type = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(type, request.toJson());
            okHttpRequest = new Request.Builder()
                    .headers(convertHeader(request))
                    .post(body)
                    .url(request.baseUrl() + request.secondUrl())
                    .build();
        }

        call = getOkHttpClient(request, eventListener).newCall(okHttpRequest);

        if (calls == null) {
            calls = new ArrayList<>();
            calls.add(call);
            callMap.put(request.tag(), calls);
        } else {
            calls.add(call);
        }

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@Nullable Call call, @Nullable IOException e) {
                handleErrorCallback(listener, e);
                callMap.remove(request.tag());
            }

            @Override
            public void onResponse(@Nullable Call call, @Nullable Response response) throws IOException {

                callMap.remove(request.tag());

                if (response != null && response.body() != null) {
                    if (response.code() == 200) {
                        handleSuccessCallback(listener, response.body().string());
                    } else {
                        handleErrorCallback(listener, new IOException(response.body().string()));
                    }
                }
            }
        });
    }


    private Headers convertHeader(BaseHttpRequest request) {
        Map<String, String> headers = request.headers();
        if (headers != null) {
            Headers.Builder builder = new Headers.Builder();
            Set<String> keySet = headers.keySet();
            for (String s : keySet) {
                builder.add(s, headers.get(s));
            }
            return builder.build();
        }
        return null;
    }

    private void handleSuccessCallback(HttpListener listener, String message) {
        handler.post(() -> listener.onRequestResult(message));
    }

    private void handleErrorCallback(HttpListener listener, IOException e) {
        handler.post(() -> listener.onRequestError(e));
    }

    @Override
    public void cancelRequest(Object tag) {
        if (tag == null) {
            return;
        }

        List<Call> remove = callMap.remove(tag);

        Log.e("zhangxu", "OkHttp ---> cancel()  +  size = " + remove.size());
        if (remove != null) {
            for (Call call : remove) {
                if (call != null) {
                    call.cancel();
                }
            }
        }
    }

    @Override
    public void cancelAllRequest() {
        Set<Object> tags = callMap.keySet();
        for (Object tag : tags) {
            cancelRequest(tag);
        }
    }
}
