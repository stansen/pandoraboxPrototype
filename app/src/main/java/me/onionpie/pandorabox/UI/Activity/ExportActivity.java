package me.onionpie.pandorabox.UI.Activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.FrameLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dd.CircularProgressButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.onionpie.greendao.DBHelper;
import me.onionpie.greendao.PasswordTextItem;
import me.onionpie.greendao.PasswordTextItemDao;
import me.onionpie.pandorabox.Model.PasswordTextInfoModel;
import me.onionpie.pandorabox.Model.SingleCharPasswordRuleModel;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.Utils.FileUtil;
import me.onionpie.pandorabox.Utils.Sercurity;
import me.onionpie.pandorabox.Utils.StatusBarCompat;
import me.onionpie.pandorabox.Widget.TopBarFragment;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class ExportActivity extends BaseCameraActivity {

    @Bind(R.id.top_bar)
    FrameLayout mTopBar;
    @Bind(R.id.btnWithText)
    CircularProgressButton mBtnWithText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        ButterKnife.bind(this);
        mType = 3;
        addFragment(R.id.top_bar, TopBarFragment.newInstance("导出密码", ""));
        StatusBarCompat.compat(this, ContextCompat.getColor(getApplicationContext(), R.color.light_blue));
        setResult();
    }

    @OnClick(R.id.btnWithText)
    public void onClickBtn() {
        validatePasswordAndScanCode();
    }

    private void setResult() {
        mBaseInterface = new BaseInterface() {
            @Override
            void validateSuccess() {
                readLocal();
            }

            @Override
            void validateFailed() {
                showToast("验证失败");
            }
        };
    }

    private Subscription mSubscription;
    private MaterialDialog mProgressMaterialDialog;
    ArrayList<PasswordTextInfoModel> mPasswordTextInfoModels = new ArrayList<PasswordTextInfoModel>();

    private void readLocal() {
        mSubscription = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
//                List<PasswordTextItem> passwordTextItems = DBHelper.getInstance().getPasswordTextItemDao().loadAll();
                PasswordTextItemDao passwordTextItemDao = DBHelper.getInstance().getPasswordTextItemDao();
                List<PasswordTextItem> passwordTextItems = passwordTextItemDao.queryBuilder()
                        .orderDesc(PasswordTextItemDao.Properties.Id).list();
                ArrayList<PasswordTextInfoModel> passwordTextInfoModels = new ArrayList<PasswordTextInfoModel>();
                for (PasswordTextItem temp : passwordTextItems) {
                    PasswordTextInfoModel passwordTextInfoModel = new PasswordTextInfoModel();
                    passwordTextInfoModel.id = temp.getId();
                    passwordTextInfoModel.jsonString = temp.getJsonString();
                    passwordTextInfoModel.description = temp.getDescription();
                    passwordTextInfoModel.time = temp.getDate();
                    passwordTextInfoModel.akString = temp.getAk();
                    Log.d("akstring", passwordTextInfoModel.akString);
                    String realPassword = "";
                    try {
                        String decryptedString = Sercurity.aesDecrypt(passwordTextInfoModel.jsonString
                                , passwordTextInfoModel.akString.getBytes());
                        Gson gson = new Gson();
                        ArrayList<SingleCharPasswordRuleModel> singleCharPasswordRuleModels =
                                gson.fromJson(decryptedString,
                                        new TypeToken<ArrayList<SingleCharPasswordRuleModel>>() {
                                        }.getType());
                        HashSet<String> ruleNames = new HashSet<String>();
                        for (SingleCharPasswordRuleModel singleCharPasswordRuleModel : singleCharPasswordRuleModels) {
                            realPassword += singleCharPasswordRuleModel.mTargetChar;
                            if (!TextUtils.isEmpty(singleCharPasswordRuleModel.mRuleName))
                                ruleNames.add(singleCharPasswordRuleModel.mRuleName);
                            passwordTextInfoModel.passwordPreview += singleCharPasswordRuleModel.mDestinyChar;
                        }
                        passwordTextInfoModel.realPassword = realPassword;
                        if (ruleNames.size() == 0) {
                            passwordTextInfoModel.ruleName = "";
                        } else {
                            for (String tempName : ruleNames) {
                                passwordTextInfoModel.ruleName += tempName + ",";
                            }
                        }
                        if (passwordTextInfoModel.ruleName.endsWith(",")) {
                            passwordTextInfoModel.ruleName = passwordTextInfoModel.ruleName.substring(0, passwordTextInfoModel.ruleName.length() - 1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    passwordTextInfoModels.add(passwordTextInfoModel);
                }
                subscriber.onNext(doExport(passwordTextInfoModels));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
//                        mProgressMaterialDialog = new MaterialDialog.Builder(getBaseContext())
//                                .customView(R.layout.single_progress, false)
//                                .show();
                    }
                }).subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mProgressMaterialDialog.dismiss();
                    }

                    @Override
                    public void onNext(Boolean result) {
                        if (result) {
                            showToast("导出成功");
                        } else showToast("导出失败");
//                        mProgressMaterialDialog.dismiss();
//                        mPasswordTextInfoModels = passwordTextInfoModels;

                    }
                });
        addSubscription(mSubscription);
    }

    private boolean doExport(ArrayList<PasswordTextInfoModel> passwordTextInfoModels) {
        File temp = new File(FileUtil.getSDCardPath() + File.separator + "pandoraBox");
//        File temp = get().getDir("pandora", Context.MODE_PRIVATE);
        if (!temp.exists()) {
            if (temp.mkdir()) {
                Log.v("pandora_file", "success");
            }
        }
//        if (!folder.exists())
//            folder.mkdir();
        File file = new File(temp.getAbsolutePath() + File.separator + "pandora.txt");
        try {
            FileWriter fileWriter = new FileWriter(file);
            for (PasswordTextInfoModel textInfoModel : passwordTextInfoModels) {
                String destiny = "描述：" + textInfoModel.description + "  " + "密码预览:" + textInfoModel.passwordPreview + "  " + "真实密码:" +
                        textInfoModel.realPassword + "  " + "规则名:" + textInfoModel.ruleName + "  " + "时间：" + textInfoModel.time;
                fileWriter.write(destiny + "\r\n");
            }
            fileWriter.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
