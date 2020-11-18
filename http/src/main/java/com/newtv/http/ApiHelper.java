package com.newtv.http;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.newtv.http.internal.HttpListener;
import com.newtv.http.internal.HttpService;
import com.newtv.http.request.BaseHttpRequest;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * @author ZhangXu
 * @date 2020/9/28
 */
public class ApiHelper {


    private static final HttpService httpService = HttpServiceImpl.getInstance();

    public static <T> Observable<T> send(BaseHttpRequest request, TypeReference<T> type) {
        return send(request, type,null);
    }

    public static <T> Observable<T> send(BaseHttpRequest request, TypeReference<T> type, EventListener eventListener) {


        return Observable.create(emitter -> httpService.sendRequest(request, new HttpListener() {
            @Override
            public void onRequestResult(String result) {
                T r = JSON.parseObject(result, type);
                if (r == null) {
                    return;
                }
                emitter.onNext(r);
            }

            @Override
            public void onRequestError(Throwable e) {
                emitter.onError(e);
            }
        }, eventListener));
    }

    public static void cancelRequest(Object tag) {
        httpService.cancelRequest(tag);
    }

    /**
     * 取消所有请求
     */
    public static void cancelAllRequest() {
        httpService.cancelAllRequest();
    }
}
