package me.onionpie.pandorabox.UI.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import me.onionpie.pandorabox.Model.PasswordDetailModel;
import me.onionpie.pandorabox.Model.PasswordTextInfoModel;
import me.onionpie.pandorabox.Model.SingleCharPasswordRuleModel;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.Rx.Event.UpdatePasswordListEvent;
import me.onionpie.pandorabox.Rx.RxBus;
import me.onionpie.pandorabox.Utils.AppManager;
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
    private static final String POSITION = "position";
    private HashSet<String> mRuleNames = new HashSet<>();
    private int mPosition;
    private ArrayList<SingleCharPasswordRuleModel> mPasswordRuleModels = new ArrayList<>();
    private PasswordTextInfoModel mPasswordTextInfoModel = new PasswordTextInfoModel();

    public static Intent getStartIntent(Context context, boolean isAdd
            , PasswordTextInfoModel passwordTextInfoModel,int position) {
        Intent starter = new Intent(context, PasswordDetailActivity.class);
        starter.putExtra(IS_ADD, isAdd);
        starter.putExtra(PASSWORD_ITEM, passwordTextInfoModel);
        starter.putExtra(POSITION,position);
        return starter;
    }

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

        if (!mPasswordTextInfoModel.realPassword.equals(mPassword.getText().toString()) && !mIsAdd) {
            initSingleCharModelList(mPassword.getText().toString());
            mRuleName.setText("");
        }
        mPasswordPreview.setText(sequence);
    }

    private void getData() {
        mIsAdd = getIntent().getBooleanExtra(IS_ADD, true);
        mPasswordTextInfoModel = getIntent().getParcelableExtra(PASSWORD_ITEM);
        if (mPasswordTextInfoModel == null)
            mPasswordTextInfoModel = new PasswordTextInfoModel();
        mPosition = getIntent().getIntExtra(POSITION,0);
    }

    private void fillView() {
        mPassword.setText(mPasswordTextInfoModel.realPassword);
        mRuleName.setText(mPasswordTextInfoModel.ruleName);
        mDescriptionET.setText(mPasswordTextInfoModel.description);
        mPassword.setSelection(mPasswordTextInfoModel.realPassword.length());
//        mDescriptionET.setSelection(mPasswordTextInfoModel.description.length()-1);
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
                    mPasswordRuleModels = singleCharPasswordRuleModels;
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
                for (String temp : mRuleNames) {
                    name += temp + ",";
                }
                if (name.endsWith(",")) {
                    mRuleName.setText(name.substring(0, name.length() - 1));
                } else
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
                    for (String temp : mRuleNames) {
                        name += temp + ",";
                    }
                    if (name.endsWith(",")) {
                        mRuleName.setText(name.substring(0, name.length() - 1));
                    } else
                        mRuleName.setText(name);
                    mPasswordPreview.setText(previewPassword);

                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onClickConfirm() {
        if (TextUtils.isEmpty(mPassword.getText().toString())) {
            showToast("请输入密码");
        } else if (TextUtils.isEmpty(mDescriptionET.getText().toString())) {
            showToast("请输入密码描述");
        } else {
            if (TextUtils.isEmpty(mRuleName.getText().toString()))
                mPasswordTextInfoModel.ruleName = "";
            else
                mPasswordTextInfoModel.ruleName = mRuleName.getText().toString();
            if (mPasswordRuleModels.size() == 0) {
                initSingleCharModelList(mPassword.getText().toString());
            }
            mPasswordTextInfoModel.passwordPreview = mPasswordPreview.getText().toString();
            mPasswordTextInfoModel.realPassword = mPassword.getText().toString();
            mPasswordTextInfoModel.description = mDescriptionET.getText().toString();
            mPasswordTextInfoModel.time = TimeUtils.getCurrentTimeInString();
            saveToLocal();
        }

    }

    private void initSingleCharModelList(String realPassword) {
        mPasswordRuleModels.clear();
        char[] chars = realPassword.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            SingleCharPasswordRuleModel ruleModel = new SingleCharPasswordRuleModel();
            ruleModel.mRuleId = i;
            ruleModel.mTargetChar = String.valueOf(chars[i]);
            ruleModel.mDestinyChar = ruleModel.mTargetChar;
            mPasswordRuleModels.add(ruleModel);
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
                long id;
                try {
                    String destiny = Sercurity.aesEncrypt(jsonString, ak);
                    String akString = new String(ak);
                    akString = String.copyValueOf(akString.toCharArray(), 0, ak.length);
                    Log.d("akstring", akString);
                    if (mIsAdd) {
                        PasswordTextItem passwordTextItem = new PasswordTextItem(null,
                                destiny, mPasswordTextInfoModel.description, akString,
                                mPasswordTextInfoModel.time);
                        id = DBHelper.getInstance().getPasswordTextItemDao().insert(passwordTextItem);
                        mPasswordTextInfoModel.id = id;
                    } else {
                        id = mPasswordTextInfoModel.id;
                        PasswordTextItem passwordTextItem = new PasswordTextItem(mPasswordTextInfoModel.id,
                                destiny, mPasswordTextInfoModel.description, akString,
                                mPasswordTextInfoModel.time);
                        DBHelper.getInstance().getPasswordTextItemDao().update(passwordTextItem);
                    }
                    mPasswordTextInfoModel.jsonString = destiny;
                    subscriber.onNext(id + "success");
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
                        showToast("compelte");
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
                        saveSuccess();
                    }
                });
    }

    private void saveSuccess() {
        if (RxBus.getInstance().hasObservers()) {
            UpdatePasswordListEvent passwordListEvent = new UpdatePasswordListEvent();
            passwordListEvent.mPasswordTextInfoModel = mPasswordTextInfoModel;
            passwordListEvent.mIsAdd = mIsAdd;
            passwordListEvent.position = mPosition;
            RxBus.getInstance().send(passwordListEvent);
        }
//        Intent intent = new Intent();
//        intent.putExtra("is_changed", true);
//        setResult(RESULT_OK, intent);
        onBackPressed();
    }

    private MaterialDialog mMaterialDialogProgress;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null && mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }
}
