package com.newtv.http;

import android.util.Log;

import androidx.annotation.Nullable;

import com.newtv.http.call.Call;
import com.newtv.http.internal.HttpListener;
import com.newtv.http.internal.HttpService;
import com.newtv.http.request.BaseHttpRequest;

import java.util.ArrayList;
import java.util.List;
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

    private final Map<Object, List<Call>> callMap = new ConcurrentHashMap<>();


    @Override
    public void sendRequest(BaseHttpRequest request, HttpListener listener, @Nullable EventListener eventListener) {
        Call call;
        List<Call> calls = callMap.get(request.tag());
        if (calls == null) {
            calls = new ArrayList<>();
            callMap.put(request.tag(), calls);
        }
        NewHttpClient client = new NewHttpClient.Builder()
                .addInterceptors(request.httpConfig().getInterceptors())
                .eventListener(eventListener)
                .build();
        call = client.newCall(request);

        calls.add(call);

        List<Call> finalCalls = calls;
        call.enqueue(new HttpListener() {
            @Override
            public void onRequestResult(String result) {
                Log.e("zhangxu", "HttpServiceImpl>>>>>>   success =" + result);
                listener.onRequestResult(result);
                finalCalls.remove(call);
            }

            @Override
            public void onRequestError(Throwable e) {

                Log.e("zhangxu", "HttpServiceImpl>>>>>>   error =" + e.toString());
                listener.onRequestError(e);
                finalCalls.remove(call);
            }
        });
    }

    @Override
    public void cancelRequest(Object tag) {
        if (callMap.containsKey(tag)) {
            List<Call> calls = callMap.remove(tag);
            Log.e("zhangxu", "calls.size = " + calls.size());
            for (Call call : calls) {
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
