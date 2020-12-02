package com.newtv.example.Request;

import android.content.Context;

import com.newtv.example.baseRequest.CmsPostBaseHttpRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhangXu
 * @date 2020/12/1
 */
public class ActiveRequest extends CmsPostBaseHttpRequest {

    public String mac;
    public String key;
    public String channelId;
    public String ts;
    public String token;

    public ActiveRequest(Context context) {
        super(context);
    }

    @Override
    public String secondUrl() {
        return "monkeyking/service/apps/activate";
    }


    @Override
    public Map<String, String> params() {
        Map<String, String> params = new HashMap<>();
        params.put("mac", mac);
        params.put("key", key);
        params.put("channelId", channelId);
        params.put("ts", ts);
        params.put("token", token);
        return params;
    }
}
