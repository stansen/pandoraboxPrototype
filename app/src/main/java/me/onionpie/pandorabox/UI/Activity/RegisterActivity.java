package me.onionpie.pandorabox.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rxjavahttprequest.service.ServiceFactory;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.onionpie.pandorabox.ConstansParam.UrlAddress;
import me.onionpie.pandorabox.Model.ResponseModel;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.Service.UserService;
import me.onionpie.pandorabox.Utils.InputTypeCheck;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RegisterActivity extends BaseActivity {

    @Bind(R.id.register_icon)
    ImageView mRegisterIcon;
    @Bind(R.id.account_name)
    MaterialEditText mAccountName;
    @Bind(R.id.auth_code)
    MaterialEditText mAuthCode;
    @Bind(R.id.send_auth_code)
    Button mSendAuthCode;
    @Bind(R.id.password)
    MaterialEditText mPassword;
    @Bind(R.id.password_confirm)
    MaterialEditText mPasswordConfirm;
    @Bind(R.id.to_register)
    Button mToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        RxView.clicks(mToRegister).throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        registerValidate();
                    }
                });
    }

    private void toRegister(String phone, String authCode, String password) {
       Subscription subscription = ServiceFactory.getInstance(this, UrlAddress.host).getService(UserService.class).register(phone, password).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("register",e.toString());
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        Log.d("register",responseModel.toString());
                        if (!responseModel.error){
                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                            intent.putExtra("account",mAccountName.getText().toString());
                            intent.putExtra("password",mPassword.getText().toString());
                            startActivity(intent);
                            showToast("success");
//                            showToast(responseModel.result);
                        }
                    }
                });
        addSubscription(subscription);
    }

    public void registerValidate() {
        String phone = mAccountName.getText().toString();
        String authCode = mAuthCode.getText().toString();
        String password = mPassword.getText().toString();
        String passwordConfirm = mPasswordConfirm.getText().toString();
        if (!InputTypeCheck.judgePhoneNums(phone)) {
            showToast("不是一个有效的号码");
        } else {
            if (!InputTypeCheck.CheckByRules(password)) {
                showToast("");
                mPassword.setError("密码为6-12位的数字字母组合");
            } else {
                if (!password.equals(passwordConfirm)) {
                    showToast("两次密码不一致");
                } else {
//                        mPasswordConfirmWrapper.setErrorEnabled(false);
                    showLoadDialog("注册", "注册中");
                    toRegister(phone, authCode, password);
                    hideLoadDialog();
                }
            }
//            if (authCode.isEmpty()) {
//                showToast("请输入验证码");
//            } else {
//
//            }
        }
    }
}
