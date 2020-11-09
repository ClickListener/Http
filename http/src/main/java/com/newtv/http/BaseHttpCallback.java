package com.newtv.http;

import com.alibaba.fastjson.TypeReference;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author ZhangXu
 * @date 2020/9/28
 */
public abstract class BaseHttpCallback<T> extends TypeReference<T> implements HttpCallback <T> {

}
