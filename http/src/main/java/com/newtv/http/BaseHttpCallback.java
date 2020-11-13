package com.newtv.http;

import com.alibaba.fastjson.TypeReference;

/**
 * @author ZhangXu
 * @date 2020/9/28
 */
public abstract class BaseHttpCallback<T> extends TypeReference<T> implements HttpCallback <T> {

}
