package com.newtv.http;


import com.newtv.http.factory.HttpServiceFactory;

/**
 * @author ZhangXu
 * @date 2020/9/28
 */
public class HttpServiceProvider {

    private HttpServiceProvider() {}

    public static HttpService createHttpService() {
        return HttpServiceFactory.createHttpService();
    }
}
