package com.newtv.example.Request;

import android.content.Context;

import com.newtv.example.baseRequest.CmsGetBaseHttpRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhangXu
 * @date 2020/11/10
 */
public class SuggestRequest extends CmsGetBaseHttpRequest {


    public String firstCategoryId="";
    public String categoryId="";
    public String contentType="";
    public String videoType="";
    public String videoClass="";
    public String area="";
    public String year="";
    public String keyword="";
    public String page="";
    public String rows="";
    public String keywordType="";
    public String orderby="";
    public String isLight="";

    public SuggestRequest(Context context) {
        super(context);
    }


    @Override
    public String getSecondUrl() {
        return "api/v31/dcf09be0f8993e896ba4de940f1692c3/50000138/search.json";
    }

    @Override
    public Map<String, String> getParams() {

        Map<String, String> params = new HashMap<>();
        params.put("firstCategoryId", firstCategoryId);
        params.put("categoryId", categoryId);
        params.put("contentType", contentType);
        params.put("videoType", videoType);
        params.put("videoClass", videoClass);
        params.put("area", area);
        params.put("year", year);
        params.put("keyword", keyword);
        params.put("page", page);
        params.put("rows", rows);
        params.put("keywordType", keywordType);
        params.put("orderby", orderby);
        params.put("isLight", isLight);
        return params;
    }
}
