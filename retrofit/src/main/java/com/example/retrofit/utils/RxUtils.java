package com.example.retrofit.utils;


import android.support.v7.app.AppCompatActivity;

import com.safframework.lifecycle.RxLifecycle;
import com.safframework.utils.RxJavaUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

public class RxUtils {

    /**
     * 对RxView绑定的时间
     * 封装了防止重复点击和RxLifecycle的生命周期
     */
    public static ObservableTransformer useRxViewTransformer(final AppCompatActivity targetActivity){

        return new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.compose(RxJavaUtils.preventDuplicateClicksTransformer())
                        .compose(RxLifecycle.bind(targetActivity).toLifecycleTransformer());
            }
        };
    }
}
