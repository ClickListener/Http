package com.newtv.http;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author ZhangXu
 * @date 2020/11/6
 */
public interface MethodType {

    @IntDef({GET,POST})
    @Retention(RetentionPolicy.SOURCE)
    @interface Method{}

    int GET = 0;
    int POST = 1;
}
