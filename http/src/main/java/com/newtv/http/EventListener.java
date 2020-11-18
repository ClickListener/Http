package com.newtv.http;

/**
 *
 * 发送事件的回调
 * @author ZhangXu
 * @date 2020/11/17
 */
public abstract class EventListener {


    public static final EventListener NONE = new EventListener() {};

    public void callStart() {}

    public void callEnd() {}

    public void callFailed() {}

}
