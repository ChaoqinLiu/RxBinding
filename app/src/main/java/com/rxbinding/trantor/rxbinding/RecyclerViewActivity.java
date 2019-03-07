package com.rxbinding.trantor.rxbinding;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;
import com.rxbinding.trantor.rxbinding.adapter.MyRecyclerAdapter;
import com.rxbinding.trantor.rxbinding.app.BaseActivity;
import com.safframework.injectview.annotations.InjectView;
import com.safframework.log.L;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class RecyclerViewActivity extends BaseActivity {

    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;

    private List<String> mDatas;
    private MyRecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyelerview);

        initViews();
        initData();
    }

    private void initViews(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerview.setLayoutManager(linearLayoutManager);

        RxRecyclerView.scrollStateChanges(recyclerview)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer scrollState) throws Exception {
                        L.i("scrollState =" + scrollState);
                    }
                });
    }

    private void initData(){

        mDatas = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            mDatas.add("item" + i);
        }

        recyclerAdapter = new MyRecyclerAdapter(RecyclerViewActivity.this, mDatas);
        recyclerview.setAdapter(recyclerAdapter);
    }
}
