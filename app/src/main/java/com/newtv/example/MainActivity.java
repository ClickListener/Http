package com.newtv.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.newtv.example.Request.ActiveRequest;
import com.newtv.example.Request.SuggestRequest;
import com.newtv.example.Request.TestGetRequest;
import com.newtv.example.response.ActiveResponse;
import com.newtv.example.response.SuggestResponse;
import com.newtv.example.response.TestResponse;
import com.newtv.http.ApiHelper;
import com.newtv.http.EventListener;
import com.newtv.http.NewTypeReference;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class MainActivity extends AppCompatActivity {


    private TextView content1;
    private TextView content2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        content1 = findViewById(R.id.content1);
        content2 = findViewById(R.id.content2);

        findViewById(R.id.cancel).setOnClickListener(v -> {
            sendNewTvRequest();
            content1.postDelayed(() -> {
                ApiHelper.cancelRequest(MainActivity.this);
            }, 500);

//            sendNewTvRequest();
//            sendJinshanRequest();
        });

        findViewById(R.id.button1).setOnClickListener(v -> {
            sendNewTvRequest();
//            sendRequest();

        });

        findViewById(R.id.post_request).setOnClickListener( v -> {
            sendPostRequest();
        });
    }


    private void sendPostRequest() {
        ActiveRequest activeRequest = new ActiveRequest(MainActivity.this);
        activeRequest.mac = "009EC8CC24A0";
        activeRequest.key = "dcf09be0f8993e896ba4de940f1692c3";
        activeRequest.channelId = "50000138";
        activeRequest.ts = "1606818459575";
        activeRequest.token = "2A27CD24E86C400D3027DBB400CA240E";

        ApiHelper.send(activeRequest, new NewTypeReference<ActiveResponse>() {}).subscribe(new Observer<ActiveResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ActiveResponse activeResponse) {
                Log.e("zhangxu", activeResponse.toString());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void sendJinshanRequest() {
        TestGetRequest testGetRequest = new TestGetRequest(MainActivity.this);
        testGetRequest.a = "fy";
        testGetRequest.f = "auto";
        testGetRequest.t = "auto";
        testGetRequest.w = "hello%20world";

        Observable<TestResponse> send = ApiHelper.send(testGetRequest, new NewTypeReference<TestResponse>() {});

        send.subscribe(new Observer<TestResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull TestResponse testResponse) {
                Log.e("zhangxu", testResponse.toString());
                content2.setText(testResponse.toString());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void sendNewTvRequest() {

        SuggestRequest request = new SuggestRequest(MainActivity.this);
        request.contentType = "PS";
        request.videoType = "%E7%94%B5%E8%A7%86%E5%89%A7";

        Observable<SuggestResponse> send = ApiHelper.send(request, new NewTypeReference<SuggestResponse>() {
        }, new EventListener() {
            @Override
            public void callStart() {
                super.callStart();
                Log.e("zhangxu", "callStart()");
            }

            @Override
            public void callEnd() {
                super.callEnd();
                Log.e("zhangxu", "callEnd()");
            }

            @Override
            public void callFailed() {
                super.callFailed();
                Log.e("zhangxu", "callFailed()");
            }
        });

        send.subscribe(new Observer<SuggestResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull SuggestResponse suggestResponse) {
                Log.e("zhangxu", "result = " + suggestResponse.toString());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    // 测试顺序发送
    private void sendRequest() {
        Log.e("zhangxu", "开始发送");

        SuggestRequest request = new SuggestRequest(MainActivity.this);
        request.contentType = "PS";
        request.videoType = "%E7%94%B5%E8%A7%86%E5%89%A7";

        Observable<SuggestResponse> suggestRequest = ApiHelper.send(request, new NewTypeReference<SuggestResponse>(){});

        suggestRequest.onErrorReturn(new Function<Throwable, SuggestResponse>() {
            @Override
            public SuggestResponse apply(@NonNull Throwable throwable) throws Exception {
                Log.e("zhangxu", "第一个请求失败");
                return null;
            }
        }).flatMap(new Function<SuggestResponse, ObservableSource<TestResponse>>() {
            @Override
            public ObservableSource<TestResponse> apply(@NonNull SuggestResponse suggestResponse) throws Exception {

                Log.e("zhangxu", "flatMap>>>>> apply()" + (suggestResponse == null));

                Log.e("zhangxu", "result = " + suggestResponse.toString());
                content1.setText(suggestResponse.toString());

                TestGetRequest testGetRequest = new TestGetRequest(MainActivity.this);
                testGetRequest.a = "fy";
                testGetRequest.f = "auto";
                testGetRequest.t = "auto";
                testGetRequest.w = "hello%20world";

                return ApiHelper.send(testGetRequest, new NewTypeReference<TestResponse>() {});
            }
        }).subscribe(new Observer<TestResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull TestResponse testResponse) {
                Log.e("zhangxu", testResponse.toString());
                content2.setText(testResponse.toString());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}