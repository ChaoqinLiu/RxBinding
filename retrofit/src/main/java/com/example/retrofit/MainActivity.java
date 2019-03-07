package com.example.retrofit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.retrofit.app.BaseActivity;
import com.example.retrofit.utils.RxUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.safframework.injectview.annotations.InjectView;

import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {

    @InjectView(R.id.text1)
    TextView text1;

    @InjectView(R.id.text2)
    TextView text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews(){

        RxView.clicks(text1)
                .compose(RxUtils.useRxViewTransformer(MainActivity.this))
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Intent intent = new Intent(MainActivity.this,RxAndroidToGetBitmapActivity.class);
                        startActivity(intent);
                    }
                });

        RxView.clicks(text2)
                .compose(RxUtils.useRxViewTransformer(MainActivity.this))
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Intent intent = new Intent(MainActivity.this,RxAndroidForOthertBitmapActivity.class);
                        startActivity(intent);
                    }
                });

    }
}
