package com.newtv.http.request;

import android.content.Context;


import com.newtv.http.config.HttpConfig;
import com.newtv.http.MethodType;

import java.io.Serializable;
import java.util.Map;

/**
 * @author ZhangXu
 * @date 2020/9/28
 */
public abstract class BaseHttpRequest implements Serializable {


    private Context mContext;


    public BaseHttpRequest(Context context) {
        this.mContext = context;
    }

    /**
     * 获得当前Request的标识
     * @return context
     */
    public Object getTag() {
        return mContext;
    }

    /**
     * 服务器地址
     * @return 正式服务器或测试服务器
     */
    public abstract String getBaseUrl();

    /**
     * 请求URL
     * @return
     */
    public abstract String getSecondUrl();

    /**
     * 获得头信息
     * @return
     */
    public abstract Map<String, String> getHeaders();

    public abstract String toJson();

    public abstract HttpConfig getHttpConfig();

    public abstract Map<String, String> getParams();

    public abstract @MethodType.Method int getMethodType();

}
