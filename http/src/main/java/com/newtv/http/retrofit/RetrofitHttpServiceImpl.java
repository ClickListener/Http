package com.newtv.http.retrofit;


import com.newtv.http.HttpListener;
import com.newtv.http.HttpService;
import com.newtv.http.MethodType;
import com.newtv.http.config.HttpConfig;
import com.newtv.http.config.RetryParam;
import com.newtv.http.request.BaseHttpRequest;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author ZhangXu
 * @date 2020/9/28
 */
public class RetrofitHttpServiceImpl implements HttpService {


    private static final String TAG = RetrofitHttpServiceImpl.class.getSimpleName();


    private Map<String, Retrofit> retrofitCache = new HashMap<>();
    /**
     * 用于保存每个页面上的请求
     */
    private ConcurrentHashMap<Object, CompositeDisposable> compositeDisposableMapping = new ConcurrentHashMap<>();


    private static volatile RetrofitHttpServiceImpl INSTANCE;

    public static RetrofitHttpServiceImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (RetrofitHttpServiceImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RetrofitHttpServiceImpl();
                }
            }
        }
        return INSTANCE;
    }

    private RetrofitHttpServiceImpl() {
    }

    private Retrofit getDefaultRetrofit(BaseHttpRequest request) {

        String baseUrl = request.getBaseUrl();
        Retrofit retrofit = retrofitCache.get(baseUrl);
        if (retrofit == null) {

            OkHttpClient okHttpClient = getOkHttpClient(request);

            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .baseUrl(baseUrl)
                    .build();
            retrofitCache.put(baseUrl, retrofit);
        }
        return retrofit;

    }

//    private Interceptor convertInterceptor(NewInterceptor newInterceptor) {
//        if (newInterceptor == null) {
//            return null;
//        }
//
//        return new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                return newInterceptor.intercept(chain);
//            }
//        };
//    }

    private OkHttpClient getOkHttpClient(BaseHttpRequest request) {

        HttpConfig config = request.getHttpConfig();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        // 如果配置不为空，则设置
        if (config != null) {

            if (config.getConnectTimeout() > 0) {
                builder.connectTimeout(config.getConnectTimeout(), config.getConnectTimeoutTimeUnit());
            }
            if (config.getReadTimeout() > 0) {
                builder.readTimeout(config.getReadTimeout(), config.getReadTimeoutTimeUnit());
            }

            if (config.getWriteTimeout() > 0) {
                builder.readTimeout(config.getWriteTimeout(), config.getWriteTimeoutTimeUnit());
            }

            for (Interceptor interceptor : config.getInterceptors()) {
                builder.addInterceptor(interceptor);
            }
        }

        // 默认的一些设置
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        TrustAllCerts.TrustAllParams params = TrustAllCerts.createSSLSocketFactory();

//        return builder.eventListenerFactory(HttpEventListener.Companion.getFACTORY())
//                .sslSocketFactory(params.sSLSocketFactory, params.trustManager)
//                .hostnameVerifier(new TrustAllCerts.TrustAllHostnameVerifier())
//                .addInterceptor(logInterceptor)
//                .build();
        return builder.sslSocketFactory(params.sSLSocketFactory, params.trustManager)
                .hostnameVerifier(new TrustAllCerts.TrustAllHostnameVerifier())
                .addInterceptor(logInterceptor)
                .build();
    }

    @Override
    public void sendRequest(BaseHttpRequest request, HttpListener listener) {
        if (request == null) {
            return;
        }
        Retrofit retrofit = getDefaultRetrofit(request);
        ApiService apiService = retrofit.create(ApiService.class);
        Observable<ResponseBody> observable;
        if (request.getMethodType() == MethodType.GET) {
            observable = apiService.getWithField(request.getHeaders(), request.getSecondUrl(), request.getParams());
        } else {
            MediaType type = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(type, request.toJson());
            observable = apiService.postWithBody(request.getHeaders(), request.getSecondUrl(), body);
        }
        // 增加重试选项
        if (request.getHttpConfig() != null && request.getHttpConfig().getRetryParam() != null) {
            RetryParam retryParam = request.getHttpConfig().getRetryParam();
            observable = observable.retryWhen(new RetryWithDelay(retryParam.getMaxRetryCount(),
                    retryParam.getRetryDelay(), retryParam.getTimeUnit()));
        }
        if (request.getRetryWithDelay() != null) {
            observable = observable.retryWhen(request.getRetryWithDelay());
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {

                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {

                        Object tag = request.getTag();
                        if (tag == null) {
                            return;
                        }
                        CompositeDisposable compositeDisposable = compositeDisposableMapping.get(tag);
                        if (compositeDisposable == null) {
                            compositeDisposable = new CompositeDisposable();
                            compositeDisposableMapping.put(tag, compositeDisposable);
                        }
                        disposable = d;
                        compositeDisposable.add(d);

                    }

                    @Override
                    public void onNext(ResponseBody o) {

                        try {
                            listener.onRequestResult(o.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onRequestError(e);

                        releaseRequest(request, disposable);
                    }

                    @Override
                    public void onComplete() {

                        releaseRequest(request, disposable);
                    }
                });

    }

    private void releaseRequest(BaseHttpRequest request, Disposable disposable) {
        Object tag = request.getTag();
        if (tag != null) {
            CompositeDisposable compositeDisposable = compositeDisposableMapping.get(tag);
            if (compositeDisposable != null) {
                compositeDisposable.remove(disposable);
                if (compositeDisposable.size() == 0) {
                    compositeDisposable.clear();
                    compositeDisposableMapping.remove(tag);
                }
            }
        }
    }

    @Override
    public void cancelRequest(Object tag) {
        if (tag == null) {
            return;
        }

        CompositeDisposable remove = compositeDisposableMapping.remove(tag);
        if (compositeDisposableMapping.contains(tag)) {
            remove.dispose();
            remove.clear();

        }
    }

    @Override
    public void cancelAllRequest() {
        Set<Object> tags = compositeDisposableMapping.keySet();
        for (Object tag : tags) {
            cancelRequest(tag);
        }
    }
}
