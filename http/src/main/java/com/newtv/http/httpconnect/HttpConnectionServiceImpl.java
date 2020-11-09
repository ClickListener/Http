package com.newtv.http.httpconnect;

import com.newtv.http.request.BaseHttpRequest;
import com.newtv.http.HttpListener;
import com.newtv.http.HttpService;

import java.net.HttpURLConnection;

/**
 * @author ZhangXu
 * @date 2020/11/9
 */
public class HttpConnectionServiceImpl implements HttpService {

    private static volatile HttpConnectionServiceImpl INSTANCE;


    private HttpConnectionServiceImpl() {}

    public static HttpConnectionServiceImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpConnectionServiceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpConnectionServiceImpl();
                }
            }
        }
        return INSTANCE;
    }
    @Override
    public void sendRequest(BaseHttpRequest request, HttpListener listener) {

    }

    @Override
    public void cancelRequest(Object tag) {

    }

    @Override
    public void cancelAllRequest() {

    }
}
