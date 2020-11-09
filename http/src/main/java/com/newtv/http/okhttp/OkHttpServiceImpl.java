package com.newtv.http.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.newtv.http.request.BaseHttpRequest;
import com.newtv.http.HttpListener;
import com.newtv.http.HttpService;
import com.newtv.http.MethodType;


import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author ZhangXu
 * @date 2020/11/6
 */
public class OkHttpServiceImpl implements HttpService {


    private static volatile OkHttpServiceImpl INSTANCE;

    private OkHttpClient mClient;

    private final Map<Object, Call> callMap= new ConcurrentHashMap<>();

    private final Handler handler = new Handler(Looper.getMainLooper());

    private OkHttpServiceImpl() {
        mClient = new OkHttpClient();
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

    @Override
    public void sendRequest(BaseHttpRequest request, HttpListener listener) {
        Call call;
        if (!callMap.containsKey(request.getTag())) {
            Request okHttpRequest;

            if (request.getMethodType() == MethodType.GET) {

                // TODO: 2020/11/6 zhangxu 添加头等信息
                okHttpRequest = new Request.Builder()
                        .url("")
                        .get()
                        .build();
            } else {
                // TODO: 2020/11/6 zhangxu 添加body 相关信息
                okHttpRequest = new Request.Builder()
                        .url(request.getBaseUrl())
                        .build();
            }

            call = mClient.newCall(okHttpRequest);
            callMap.put(request.getTag(), call);
        } else {
            call = callMap.get(request.getTag());
        }

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@Nullable Call call, @Nullable IOException e) {
                handleErrorCallback(listener, e);
                callMap.remove(request.getTag());
            }

            @Override
            public void onResponse(@Nullable Call call, @Nullable Response response) throws IOException {

                callMap.remove(request.getTag());

                if (response != null && response.body() != null) {
                    handleSuccessCallback(listener, response.toString());
                }
            }
        });

    }

    private void handleSuccessCallback(HttpListener listener, String message) {
        handler.post(() -> listener.onRequestResult(message));
    }

    private void handleErrorCallback(HttpListener listener, IOException e) {
        handler.post(() -> listener.onRequestError(e));
    }

    @Override
    public void cancelRequest(Object tag) {
        if (callMap.containsKey(tag)) {
            Call call = callMap.get(tag);
            if (call != null) {
                call.cancel();
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
