package com.newtv.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.newtv.example.Request.SuggestRequest;
import com.newtv.example.Request.TestGetRequest;
import com.newtv.example.response.SuggestResponse;
import com.newtv.example.response.TestResponse;
import com.newtv.http.ApiHelper;
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
            ApiHelper.cancelRequest(MainActivity.this);
        });

        findViewById(R.id.button1).setOnClickListener(v -> {

            sendRequest();

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