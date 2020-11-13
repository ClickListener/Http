package com.newtv.example;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.newtv.example.Request.SuggestRequest;
import com.newtv.example.response.SuggestResponse;
import com.newtv.http.ApiHelper;
import com.newtv.http.BaseHttpCallback;

public class MainActivity extends AppCompatActivity {


    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textView = findViewById(R.id.content);

        findViewById(R.id.button1).setOnClickListener(v -> {
            SuggestRequest request = new SuggestRequest(MainActivity.this);
            request.contentType = "PS";
            request.videoType = "%E7%94%B5%E8%A7%86%E5%89%A7";

            ApiHelper.send(request, new BaseHttpCallback<SuggestResponse>() {

                @Override
                public void success(SuggestResponse o) {
                    Log.e("zhangxu", "result = " + o.toString());
                    textView.setText(o.toString());
                }

                @Override
                public void error(Object result) {

                }
            });
        });


    }
}