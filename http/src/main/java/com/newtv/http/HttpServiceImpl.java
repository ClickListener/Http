package com.newtv.http;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.newtv.http.call.Call;
import com.newtv.http.internal.HttpListener;
import com.newtv.http.internal.HttpService;
import com.newtv.http.request.BaseHttpRequest;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ZhangXu
 * @date 2020/11/17
 */
public class HttpServiceImpl implements HttpService {

    private static volatile HttpServiceImpl INSTANCE;

    private HttpServiceImpl() {}

    protected static HttpServiceImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpServiceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpServiceImpl();
                }
            }
        }
        return INSTANCE;
    }

    private final Map<Object, Call> callMap = new ConcurrentHashMap<>();


    @Override
    public void sendRequest(BaseHttpRequest request, HttpListener listener, @Nullable EventListener eventListener) {
        Call call;
        if (callMap.containsKey(request.getTag())) {
            call = callMap.get(request.getTag());
        } else {
            NewHttpClient client = new NewHttpClient.Builder()
                    .addInterceptors(request.getHttpConfig().getInterceptors())
                    .eventListener(eventListener)
                    .build();
            call = client.newCall(request);
            callMap.put(request.getTag(), call);
        }
        if (call != null) {
            call.enqueue(new HttpListener() {
                @Override
                public void onRequestResult(String result) {
                    listener.onRequestResult(result);
                    callMap.remove(request.getTag());
                }

                @Override
                public void onRequestError(Throwable e) {
                    listener.onRequestError(e);
                    callMap.remove(request.getTag());
                }
            });
        }
    }

    @Override
    public void cancelRequest(Object tag) {
        if (callMap.containsKey(tag)) {
            Call call = callMap.remove(tag);
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
