package com.rxbinding.trantor.rxbinding;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.rxbinding.trantor.rxbinding.app.BaseActivity;
import com.rxbinding.trantor.rxbinding.utils.RxUtils;
import com.safframework.injectview.annotations.InjectView;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {

    @InjectView(R.id.btn_click)
    Button button;

    @InjectView(R.id.btn_longClick)
    Button btn_long;

    @InjectView(R.id.btn_com)
    Button btn_com;

    @InjectView(R.id.btn_form)
    Button btn_form;

    @InjectView(R.id.btn_countdown)
    Button btn_countdown;

    @InjectView(R.id.btn_recyclerview)
    Button recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews(){

        RxView.clicks(button)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception{
                        Toast.makeText(MainActivity.this,"演示点击事件",Toast.LENGTH_SHORT).show();
                    }
                });

        RxView.longClicks(btn_long)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception{
                        Toast.makeText(MainActivity.this,"演示长击时间",Toast.LENGTH_SHORT).show();
                    }
                });

        RxView.clicks(btn_com)
                .compose(RxUtils.useRxViewTransformer(MainActivity.this))
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Toast.makeText(MainActivity.this,"防止重复点击",Toast.LENGTH_SHORT).show();
                    }
                });

        RxView.clicks(btn_form)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o){
                        Intent intent = new Intent(MainActivity.this,FormValidationActivity.class);
                        startActivity(intent);
                    }
                });

        RxView.clicks(btn_countdown)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o){
                        Intent intent = new Intent(MainActivity.this,CountdownActivity.class);
                        startActivity(intent);
                    }
                });

        RxView.clicks(recyclerview)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o){
                        Intent intent = new Intent(MainActivity.this,RecyclerViewActivity.class);
                        startActivity(intent);
                    }
                });
    }
}
