package com.rxbinding.trantor.rxbinding;

import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.rxbinding.trantor.rxbinding.app.BaseActivity;
import com.safframework.injectview.annotations.InjectView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CountdownActivity extends BaseActivity {

    @InjectView(R.id.verification_code)
    EditText verification_code;

    @InjectView(R.id.get_verification_code)
    TextView get_verification_code;

    private long MAX_COUNT_TIME = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_count_down);
        initViews();
    }

    private void initViews(){
        RxView.clicks(verification_code)
                .throttleFirst(MAX_COUNT_TIME,TimeUnit.SECONDS) //间隔60秒才再次响应点击事件
                .flatMap(new Function<Object, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(Object o) throws Exception{
                        //更新发送按钮的状态，并初始化显示倒计时的文字
                        RxView.enabled(verification_code).accept(false);
                        RxTextView.text(verification_code).accept("剩余 " + MAX_COUNT_TIME + " 秒");

                        //返回n秒内的倒计时观察者对象
                        return Observable.interval(1,TimeUnit.SECONDS,Schedulers.io()).take(MAX_COUNT_TIME);
                    }
                }).map(new Function<Long, Long>() {
            //将递增数字替换成递减的倒计时数字
            @Override
            public Long apply(Long aLong) throws Exception{
                return MAX_COUNT_TIME - (aLong + 1);
            }
        }).observeOn(AndroidSchedulers.mainThread())  //切换到Android的主线程
        .subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                if (aLong == 0) {
                    RxView.enabled(verification_code).accept(true);
                    RxTextView.text(verification_code).accept("获取验证码");
                } else {
                    RxTextView.text(verification_code).accept("剩余 " + aLong + " 秒");
                }
            }
        });
    }


}
