package com.rxbinding.trantor.rxbinding;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.rxbinding.trantor.rxbinding.app.BaseActivity;
import com.rxbinding.trantor.rxbinding.utils.RxUtils;
import com.safframework.injectview.annotations.InjectView;
import com.safframework.log.L;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;
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

    @InjectView(R.id.btn_ui)
    Button listeningControl;

    @InjectView(R.id.btn_phone1)
    Button phone1;

    @InjectView(R.id.btn_phone2)
    Button phone2;

    @InjectView(R.id.btn_MultiplePermissions)
    Button multiplePermissions;

    @SuppressLint("MissingPermission")

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

        Observable clickObservable = RxView.clicks(listeningControl)
                .compose(RxUtils.useRxViewTransformer(MainActivity.this)).share();

        clickObservable.subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Toast.makeText(MainActivity.this,"对UI的第1次监听",Toast.LENGTH_SHORT).show();
            }
        });

        clickObservable.subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Toast.makeText(MainActivity.this,"对UI的第2次监听",Toast.LENGTH_SHORT).show();
            }
        });

        clickObservable.subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Toast.makeText(MainActivity.this,"对UI的第3次监听",Toast.LENGTH_SHORT).show();
            }
        });



        final RxPermissions rxPermissions = new RxPermissions(this);
        RxView.clicks(phone1)
                .subscribe(new Consumer<Object>() {

                    @Override
                    public void accept(@NonNull Object o) throws Exception{

                        rxPermissions.request(Manifest.permission.CALL_PHONE)
                                .subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(Boolean granted) throws Exception {
                                        if (granted) {
                                            Intent intent = new Intent(Intent.ACTION_CALL);
                                            intent.setData(Uri.parse("tel:" + "18230069304"));
                                            startActivity(intent);
                                        } else {
                                            L.i("授权失败");
                                        }
                                    }
                                });
                    }
                });

        RxView.clicks(phone2)
                .compose(rxPermissions.ensure(Manifest.permission.CALL_PHONE))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + "10000"));
                            startActivity(intent);
                        } else {
                            L.i("授权失败");
                        }
                    }
                });

        RxView.clicks(multiplePermissions)
                .compose(rxPermissions.ensure(Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {
                            Toast.makeText(MainActivity.this,"打开相机成功",Toast.LENGTH_SHORT).show();
                        } else {
                            L.i("授权失败");
                            Toast.makeText(MainActivity.this,"打开相机失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
