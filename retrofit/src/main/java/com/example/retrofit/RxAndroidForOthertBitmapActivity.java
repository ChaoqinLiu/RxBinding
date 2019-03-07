package com.example.retrofit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.example.retrofit.app.BaseActivity;
import com.safframework.injectview.annotations.InjectView;
import com.safframework.log.L;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxAndroidForOthertBitmapActivity extends BaseActivity {

    @InjectView(R.id.image_view1)
    ImageView imageView;

    private final static String IMAGE_URL = "http://www.designerspics.com/wp-content/uploads/2014/09/grass_shrubs_2_free_photo.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image_view);
        initViews();
    }

    private void initViews(){

        Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception{

                L.i("IMAGE_URL=" + IMAGE_URL);
                e.onNext(IMAGE_URL);
            }
        }).subscribeOn(AndroidSchedulers.from(new Handler().getLooper()))
        .observeOn(Schedulers.io())
        .map(new Function<String, Bitmap>() {

            @Override
            public Bitmap apply(String s) throws Exception {

                L.i("s=" + s);
                return getBitmap(s);
            }
        })
        // 设置图片加载在主线程中进行
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Bitmap>() {
            @Override
            public void accept(Bitmap bitmap) throws Exception {
                if (bitmap != null) {
                    L.i("birmap is not null");
                    imageView.setImageBitmap(bitmap);
                }
            }
        });

    }

    private Bitmap getBitmap(String imageUrl){

        HttpURLConnection connection;

        try {
            URL url = new URL(imageUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(20000);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                return BitmapFactory.decodeStream(connection.getInputStream());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
