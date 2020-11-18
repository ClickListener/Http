package com.newtv.http.internal;

/**
 * @author ZhangXu
 * @date 2020/9/28
 */
public interface HttpListener{


    void onRequestResult(String result);


    void onRequestError(Throwable e);
}
