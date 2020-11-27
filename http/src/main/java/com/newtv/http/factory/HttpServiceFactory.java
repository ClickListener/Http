package com.newtv.http.factory;

import com.newtv.http.internal.HttpService;
import com.newtv.http.internal.httpconnect.HttpConnectionServiceImpl;
import com.newtv.http.internal.okhttp.OkHttpServiceImpl;
import com.newtv.http.internal.retrofit.RetrofitHttpServiceImpl;

/**
 * @author ZhangXu
 * @date 2020/11/6
 */
public class HttpServiceFactory {

    public static int TYPE = 2;

    public static HttpService createHttpService() {
        if (TYPE == 0) {
            return RetrofitHttpServiceImpl.getInstance();
        } else if (TYPE == 1) {
            return OkHttpServiceImpl.getInstance();
        } else if (TYPE == 2) {
            return HttpConnectionServiceImpl.getInstance();
        } else {
            return RetrofitHttpServiceImpl.getInstance();
        }
    }
}
