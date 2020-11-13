package com.newtv.http;


import com.alibaba.fastjson.JSON;
import com.newtv.http.request.BaseHttpRequest;

/**
 * @author ZhangXu
 * @date 2020/9/28
 */
public class ApiHelper {


    public static <T> void send(BaseHttpRequest request, HttpCallback<T> callback) {
        HttpServiceProvider.createHttpService().sendRequest(request, new HttpListener() {
            @Override
            public void onRequestResult(String result) {

                T r = JSON.parseObject(result, callback.getType());
                if (r == null) {
                    return;
                }
                callback.success(r);
            }

            @Override
            public void onRequestError(Throwable e) {
                callback.error(e);
            }
        });
    }


    public static void cancelRequest(Object tag) {
        HttpServiceProvider.createHttpService().cancelRequest(tag);
    }

    /**
     * 取消所有请求
     */
    public static void cancelAllRequest() {
        HttpServiceProvider.createHttpService().cancelAllRequest();
    }
}
