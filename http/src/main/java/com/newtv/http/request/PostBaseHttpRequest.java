package com.newtv.http.request;

import android.content.Context;

import com.newtv.http.MethodType;

/**
 * @author ZhangXu
 * @date 2020/11/6
 */
public abstract class PostBaseHttpRequest extends BaseHttpRequest {
    public PostBaseHttpRequest(Context context) {
        super(context);
    }

    @Override
    public int getMethodType() {
        return MethodType.POST;
    }
}
