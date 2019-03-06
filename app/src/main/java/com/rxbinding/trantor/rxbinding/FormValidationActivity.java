package com.rxbinding.trantor.rxbinding;

import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.rxbinding.trantor.rxbinding.app.BaseActivity;
import com.rxbinding.trantor.rxbinding.domain.ValidationResult;
import com.rxbinding.trantor.rxbinding.utils.AppUtils;
import com.rxbinding.trantor.rxbinding.utils.RxUtils;
import com.safframework.injectview.annotations.InjectView;
import com.safframework.tony.common.utils.Preconditions;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

public class FormValidationActivity extends BaseActivity {

    @InjectView(R.id.text_phone)
    EditText phone;

    @InjectView(R.id.text_pwd)
    EditText password;

    @InjectView(R.id.btn_submit)
    Button login;

    private ValidationResult result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_form_validation);
        initViews();
    }

    private void initViews(){

        Observable<CharSequence> ObservablePhone = RxTextView.textChanges(phone);
        Observable<CharSequence> ObservablePassword = RxTextView.textChanges(password);

        Observable.combineLatest(ObservablePhone, ObservablePassword, new BiFunction<CharSequence, CharSequence, ValidationResult>() {

            @Override
            public ValidationResult apply(@NonNull CharSequence o1,@NonNull CharSequence o2) throws Exception{

                ValidationResult result = new ValidationResult();
                if (o1.length() == 0) {
                    result.flag = false;
                    result.message = "手机号码不能为空";
                } else if (o1.length() != 11) {
                    result.flag = false;
                    result.message = "手机号码需要11位";
                } else if (o1 != null && !AppUtils.isPhoneNumber(o1.toString())) {
                    result.flag = false;
                    result.message = "手机号码需要数字";
                } else if (o2.length() == 0) {
                    result.flag = false;
                    result.message = "密码不能为空";
                }

                return result;
            }
        }).subscribe(new Consumer<ValidationResult>() {
            @Override
            public void accept(@NonNull ValidationResult validationResult) throws Exception {

                result = validationResult;
            }
        });

        RxView.clicks(login)
                .compose(RxUtils.useRxViewTransformer(FormValidationActivity.this))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (result == null) return;
                        if (result.flag) {
                            Toast.makeText(FormValidationActivity.this,"模拟登陆成功",Toast.LENGTH_SHORT).show();
                        } else if (Preconditions.isNotBlank(result.message)){
                            Toast.makeText(FormValidationActivity.this,result.message,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
