package com.newtv.http.request;

import android.content.Context;

import com.newtv.http.MethodType;

/**
 * @author ZhangXu
 * @date 2020/11/6
 */
public abstract class GetBaseHttpRequest extends BaseHttpRequest {

    public GetBaseHttpRequest(Context context) {
        super(context);
    }

    // TODO: 2020/11/6 zhangxu  返回请求 Method


    @Override
    public int getMethodType() {
        return MethodType.GET;
    }
}
