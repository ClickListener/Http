package com.newtv.http.request;

import android.content.Context;
import android.util.Log;

import com.newtv.http.MethodType;

/**
 * @author ZhangXu
 * @date 2020/11/6
 */
public abstract class GetBaseHttpRequest extends BaseHttpRequest {

    public GetBaseHttpRequest(Context context) {
        super(context);
    }


    @Override
    public int methodType() {
        return MethodType.GET;
    }
}
