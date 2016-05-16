package me.onionpie.pandorabox.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.rxjavahttprequest.service.ServiceFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.onionpie.pandorabox.ConstansParam.UrlAddress;
import me.onionpie.pandorabox.Model.ResponseModel;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.Service.UserService;
import me.onionpie.pandorabox.Utils.InputTypeCheck;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.img_head)
    ImageView mImgHead;
    @Bind(R.id.account_name)
    MaterialEditText mAccountName;
    @Bind(R.id.password)
    MaterialEditText mPassword;
    @Bind(R.id.to_register)
    TextView mToRegister;
    @Bind(R.id.to_login)
    Button mToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getIntentData();
    }
    private void getIntentData(){
        String account = getIntent().getStringExtra("account");
        String password = getIntent().getStringExtra("password");
        mAccountName.setText(account);
        mPassword.setText(password);
        if (account !=null)
            toLogin();
    }
    @OnClick(R.id.to_register)
    public void ToRegister(){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.to_login)
    public void toLogin(){
        if(!InputTypeCheck.isMobileNO(mAccountName.getText().toString())){
            showToast("请输入正确格式的手机号");
        }else if (TextUtils.isEmpty(mPassword.getText().toString())){
            showToast("请输入密码");
        }else {
            doLogin(mAccountName.getText().toString(),mPassword.getText().toString());
        }
    }

    private void doLogin(String phone,String password){
        Subscription subscription = ServiceFactory.getInstance(this, UrlAddress.host).getService(UserService.class).login(phone, password).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("login",e.toString());
                    }

                    @Override
                    public void onNext(ResponseModel responseModel) {
                        Log.d("login",responseModel.toString());
                        if (!responseModel.error){
                            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                            startActivity(intent);
                            showToast("登录成功");
//                            showToast(responseModel.result);
                        }
                    }
                });
        addSubscription(subscription);
    }
}
