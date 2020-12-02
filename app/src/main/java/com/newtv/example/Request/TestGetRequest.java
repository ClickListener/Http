package com.newtv.example.Request;

import android.content.Context;

import com.newtv.example.baseRequest.CmsGetBaseHttpRequest;
import com.newtv.http.config.HttpConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhangXu
 * @date 2020/11/18
 */
public class TestGetRequest extends CmsGetBaseHttpRequest {

    public String a;
    public String f;
    public String t;
    public String w;

    public TestGetRequest(Context context) {
        super(context);
    }

    @Override
    public String baseUrl() {
        return "http://fy.iciba.com/";
    }

    @Override
    public String secondUrl() {
        return "ajax.php";
    }

    @Override
    public Map<String, String> params() {
        Map<String, String> params = new HashMap<>();
        params.put("a", a);
        params.put("f", f);
        params.put("t", t);
        params.put("w", w);
        return params;
    }
}
