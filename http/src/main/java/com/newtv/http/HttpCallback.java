package com.newtv.http;

import java.lang.reflect.Type;

/**
 * @author ZhangXu
 * @date 2020/9/28
 */
public interface HttpCallback<T> {

    Type getType();

    void success(T t);

    void error(Object result);
}
