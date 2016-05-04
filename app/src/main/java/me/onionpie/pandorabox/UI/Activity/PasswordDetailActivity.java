package me.onionpie.pandorabox.UI.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import me.onionpie.pandorabox.Helper.TransitionHelper;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.Widget.TopBarFragment;

public class PasswordDetailActivity extends BaseActivity {
    private static final int REQUEST_PASSWORD = 10001;
    @Bind(R.id.rule_setting)
    LinearLayout mRuleSetting;
    @Bind(R.id.password)
    MaterialEditText mPassword;
    @Bind(R.id.password_preview)
    TextView mPasswordPreview;
    @Bind(R.id.confirm)
    Button mConfirm;
    private boolean mIsAdd = true;
    private static final String IS_ADD = "is_add";
    private String mRealPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_detail);
        ButterKnife.bind(this);
        replaceFragment(R.id.top_bar, TopBarFragment.newInstance("密码详情", ""));
        getData();
        supportPostponeEnterTransition();
        if (mIsAdd) {

        } else {
            fillView();
        }

    }

    @OnClick(R.id.rule_setting)
    public void onClickRuleSetting(){
        if (!TextUtils.isEmpty(mPassword.getText().toString())){
//            startRuleSettingActivityWithTransition(this,mRuleSetting,mPassword.getText().toString(),"密码显示规则设置");
//            Intent intent = new Intent(this,RuleSettingActivity.class);
//            intent.putExtra("password",mPassword.getText().toString());
//            intent.putExtra("title",)
            Intent startIntent = RuleSettingActivity.getStartIntent(this, mPassword.getText().toString(),"密码显示规则设置");
            startActivity(startIntent);
        }


    }

    @OnTextChanged(R.id.password)
    public void onPasswordChanged(CharSequence sequence){
        mPasswordPreview.setText(sequence);
    }
    private void getData() {
        mIsAdd = getIntent().getBooleanExtra(IS_ADD, true);
    }
    private void fillView() {

    }

    @Override
    protected void onResume() {
        supportStartPostponedEnterTransition();
        super.onResume();
    }

    private void startRuleSettingActivityWithTransition(Activity activity, View toolbar,
                                                        String password,String title) {

        final Pair[] pairs = TransitionHelper.createSafeTransitionParticipants(activity, false,
                new Pair<>(toolbar, activity.getString(R.string.transition_toolbar)));
        @SuppressWarnings("unchecked")
        ActivityOptionsCompat sceneTransitionAnimation = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity, pairs);

        // Start the activity with the participants, animating from one to the other.
        final Bundle transitionBundle = sceneTransitionAnimation.toBundle();
        Intent startIntent = RuleSettingActivity.getStartIntent(activity, password,title);
        ActivityCompat.startActivityForResult(activity,
                startIntent,
                REQUEST_PASSWORD,
                transitionBundle);
    }

}
