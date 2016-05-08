package me.onionpie.pandorabox.UI.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding.view.RxView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import me.onionpie.greendao.DBHelper;
import me.onionpie.greendao.PasswordTextItem;
import me.onionpie.pandorabox.Model.PasswordTextInfoModel;
import me.onionpie.pandorabox.Model.SingleCharPasswordRuleModel;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.Utils.Sercurity;
import me.onionpie.pandorabox.Widget.TopBarFragment;
import me.onionpie.pandorabox.util.FileUtils;
import me.onionpie.pandorabox.util.TimeUtils;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class PasswordDetailActivity extends BaseActivity {
    public static final int REQUEST_PASSWORD = 10001;
    @Bind(R.id.rule_setting)
    LinearLayout mRuleSetting;
    @Bind(R.id.password)
    MaterialEditText mPassword;
    @Bind(R.id.password_preview)
    TextView mPasswordPreview;
    @Bind(R.id.confirm)
    Button mConfirm;
    @Bind(R.id.rule_name)
    TextView mRuleName;
    @Bind(R.id.description)
    MaterialEditText mDescriptionET;
    private boolean mIsAdd = true;
    private static final String IS_ADD = "is_add";
    private static final String PASSWORD_ITEM = "password_item";
    private HashSet<String> mRuleNames = new HashSet<>();
    private ArrayList<SingleCharPasswordRuleModel> mPasswordRuleModels = new ArrayList<>();
    private PasswordTextInfoModel mPasswordTextInfoModel = new PasswordTextInfoModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_detail);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        replaceFragment(R.id.top_bar, TopBarFragment.newInstance("密码详情", ""));
        getData();
//        supportPostponeEnterTransition();
        if (mIsAdd) {

        } else {
            fillView();
        }
        RxView.clicks(mConfirm).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onClickConfirm();
            }
        });
    }

    @OnClick(R.id.rule_setting)
    public void onClickRuleSetting() {
        if (!TextUtils.isEmpty(mPassword.getText().toString())) {

            Intent startIntent = RuleSettingActivity.getStartIntent(this,
                    mPassword.getText().toString(), "密码显示规则设置", mPasswordRuleModels);
            startActivityForResult(startIntent, REQUEST_PASSWORD);
        }

    }

    @OnTextChanged(R.id.password)
    public void onPasswordChanged(CharSequence sequence) {
        mPasswordPreview.setText("密码显示预览：" + sequence);
    }

    private void getData() {
        mIsAdd = getIntent().getBooleanExtra(IS_ADD, true);
        mPasswordTextInfoModel = getIntent().getParcelableExtra(PASSWORD_ITEM);
        if (mPasswordTextInfoModel == null)
            mPasswordTextInfoModel = new PasswordTextInfoModel();
    }

    private void fillView() {
        mPassword.setText(mPasswordTextInfoModel.realPassword);
        mRuleName.setText(mPasswordTextInfoModel.ruleName);
        mDescriptionET.setText(mPasswordTextInfoModel.description);
        rx.Observable.create(new rx.Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String previewPassword = "";
                try {
                    String decryptedString = Sercurity.aesDecrypt(mPasswordTextInfoModel.jsonString,
                            mPasswordTextInfoModel.akString.getBytes());
                    Gson gson = new Gson();
                    ArrayList<SingleCharPasswordRuleModel> singleCharPasswordRuleModels = gson.fromJson(decryptedString
                            , new TypeToken<ArrayList<SingleCharPasswordRuleModel>>() {
                            }.getType());
                    for (int i = 0; i < singleCharPasswordRuleModels.size(); i++) {
                        previewPassword += singleCharPasswordRuleModels.get(i).mDestinyChar;
                        if (!TextUtils.isEmpty(singleCharPasswordRuleModels.get(i).mRuleName)) {
                            mRuleNames.add(singleCharPasswordRuleModels.get(i).mRuleName);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                subscriber.onNext(previewPassword);
            }
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                mPasswordPreview.setText(s);
                String name = "";
                int position = 0;
                for (String temp : mRuleNames) {
                    if (position != mRuleNames.size() - 1)
                        name += temp + ",";
                    else name += temp;
                }
                mRuleName.setText(name);
                mPasswordTextInfoModel.ruleName = name;
            }
        });

    }

    @Override
    protected void onResume() {
//        supportStartPostponedEnterTransition();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PASSWORD:
                    ArrayList<SingleCharPasswordRuleModel> singleCharPasswordRuleModels = data.getParcelableArrayListExtra("rule_password_array");
                    mPasswordRuleModels = singleCharPasswordRuleModels;
                    String previewPassword = "";
                    for (int i = 0; i < singleCharPasswordRuleModels.size(); i++) {
                        previewPassword += singleCharPasswordRuleModels.get(i).mDestinyChar;
                        if (!TextUtils.isEmpty(singleCharPasswordRuleModels.get(i).mRuleName)) {
                            mRuleNames.add(singleCharPasswordRuleModels.get(i).mRuleName);
                        }
                    }
                    String name = "";
                    int position = 0;
                    for (String temp : mRuleNames) {
                        if (position != mRuleNames.size() - 1)
                            name += temp + ",";
                        else name += temp;
                    }
                    mRuleName.setText(name);
                    mPasswordPreview.setText("密码显示预览：" + previewPassword);

                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onClickConfirm() {
        if (TextUtils.isEmpty(mPassword.getText().toString())) {
            showToast("请输入密码");
        } else if (TextUtils.isEmpty(mRuleName.getText().toString())) {
            showToast("请设置密码规则");
        } else if (TextUtils.isEmpty(mDescriptionET.getText().toString())) {
            showToast("请输入密码描述");
        } else {
            mPasswordTextInfoModel.ruleName = mRuleName.getText().toString();
            mPasswordTextInfoModel.realPassword = mPassword.getText().toString();
            mPasswordTextInfoModel.description = mDescriptionET.getText().toString();
            mPasswordTextInfoModel.time = TimeUtils.getCurrentTimeInString();
            saveToLocal();
        }

    }

    private Subscription mSubscription;


    private void saveToLocal() {
        final byte[] ak = Sercurity.generateKey();
        mSubscription = rx.Observable.create(new rx.Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(mPasswordRuleModels);
                long id = 0;
                try {
                    String destiny = Sercurity.aesEncrypt(jsonString, ak);
                    if (mIsAdd){
                        PasswordTextItem passwordTextItem = new PasswordTextItem(null,
                                destiny, mPasswordTextInfoModel.description, Arrays.toString(ak),
                                mPasswordTextInfoModel.time);
                        id = DBHelper.getInstance().getPasswordTextItemDao().insert(passwordTextItem);
                        mPasswordTextInfoModel.id = id;
                    }else {
                        PasswordTextItem passwordTextItem = new PasswordTextItem(mPasswordTextInfoModel.id,
                                destiny, mPasswordTextInfoModel.description, Arrays.toString(ak),
                                mPasswordTextInfoModel.time);
                        DBHelper.getInstance().getPasswordTextItemDao().refresh(passwordTextItem);
                    }

                    mPasswordTextInfoModel.jsonString = destiny;
                    if (id != 0)
                        subscriber.onNext(id+"success");
                } catch (Exception e) {
                    showToast(e.toString());
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        //show progress
                        mMaterialDialogProgress = new MaterialDialog.Builder(PasswordDetailActivity.this)
                                .customView(R.layout.single_progress, false)
                                .show();
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast("error");
                        mMaterialDialogProgress.dismiss();
                    }

                    @Override
                    public void onNext(String s) {
                        mIsAdd = false;
                        mMaterialDialogProgress.dismiss();
                        showToast(s);
                    }
                });
    }

    private MaterialDialog mMaterialDialogProgress;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null && mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }
}
