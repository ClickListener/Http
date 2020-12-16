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


    private final Context mContext;


    public BaseHttpRequest(Context context) {
        this.mContext = context;
    }

    /**
     * 获得当前Request的标识
     * @return context
     */
    public final Object tag() {
        return mContext;
    }

    /**
     * 服务器地址
     * @return 正式服务器或测试服务器
     */
    public abstract String baseUrl();

    /**
     * 请求URL
     * @return
     */
    public abstract String secondUrl();

    /**
     * 获得头信息
     * @return
     */
    public abstract Map<String, String> headers();

    /**
     * 将请求转成JSON
     * @return
     */
    public abstract String toJson();

    /**
     * 网络请求的配置
     * @return
     */
    public abstract HttpConfig httpConfig();

    /**
     * 将请求参数转成MAP
     * @return
     */
    public abstract Map<String, String> params();

    /**
     * 请求方法
     * @return
     */
    public abstract @MethodType.Method int methodType();

}
