package com.newtv.http.retrofit;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * @author ZhangXu
 * @date 2020/9/28
 */
public interface ApiService {


    @POST("{url}")
    Observable<ResponseBody> postWithBody(@HeaderMap Map<String, String> headers,
                                                @Path(value = "url", encoded = true) String url,
                                                @Body RequestBody requestBody);

    @POST("{url}")
    @FormUrlEncoded
    Observable<ResponseBody> postWithField(@HeaderMap Map<String, String> headers,
                                                @Path(value = "url", encoded = true) String url,
                                                @FieldMap(encoded = true) Map<String, String> params);


    @GET("{url}")
    Observable<ResponseBody> getWithField(@HeaderMap Map<String, String> headers,
                                         @Path(value = "url", encoded = true) String url,
                                         @QueryMap(encoded = true) Map<String, String> params);
}
