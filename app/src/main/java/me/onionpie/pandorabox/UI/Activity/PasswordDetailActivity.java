package me.onionpie.pandorabox.UI.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import me.onionpie.pandorabox.Helper.TransitionHelper;
import me.onionpie.pandorabox.Model.SingleCharPasswordRuleModel;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.Utils.FileUtil;
import me.onionpie.pandorabox.Widget.TopBarFragment;
import me.onionpie.pandorabox.util.FileUtils;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
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
    private boolean mIsAdd = true;
    private static final String IS_ADD = "is_add";
    private String mRealPassword;
    private HashSet<String> mRuleNames = new HashSet<>();
    private ArrayList<SingleCharPasswordRuleModel> mPasswordRuleModels = new ArrayList<>();

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

    }

    @OnClick(R.id.rule_setting)
    public void onClickRuleSetting() {
        if (!TextUtils.isEmpty(mPassword.getText().toString())) {
            Intent startIntent = RuleSettingActivity.getStartIntent(this, mPassword.getText().toString(), "密码显示规则设置", mPasswordRuleModels);
            startActivityForResult(startIntent, REQUEST_PASSWORD);
        }


    }

    @OnTextChanged(R.id.password)
    public void onPasswordChanged(CharSequence sequence) {
        mPasswordPreview.setText("密码显示预览：" + sequence);
    }

    private void getData() {
        mIsAdd = getIntent().getBooleanExtra(IS_ADD, true);
    }

    private void fillView() {

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

    //    private void startRuleSettingActivityWithTransition(Activity activity, View toolbar,
//                                                        String password, String title) {
//
//        final Pair[] pairs = TransitionHelper.createSafeTransitionParticipants(activity, false,
//                new Pair<>(toolbar, activity.getString(R.string.transition_toolbar)));
//        @SuppressWarnings("unchecked")
//        ActivityOptionsCompat sceneTransitionAnimation = ActivityOptionsCompat
//                .makeSceneTransitionAnimation(activity, pairs);
//
//        // Start the activity with the participants, animating from one to the other.
//        final Bundle transitionBundle = sceneTransitionAnimation.toBundle();
//        Intent startIntent = RuleSettingActivity.getStartIntent(activity, password, title);
//        ActivityCompat.startActivityForResult(activity,
//                startIntent,
//                REQUEST_PASSWORD,
//                transitionBundle);
//    }
    @OnClick(R.id.confirm)
    public void onClickConfirm() {
        saveToLocal();
    }

    private Subscription mSubscription;

    private void saveToLocal() {

    }
}
