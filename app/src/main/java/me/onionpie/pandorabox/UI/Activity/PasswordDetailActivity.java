package me.onionpie.pandorabox.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
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
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.Widget.TopBarFragment;

public class PasswordDetailActivity extends BaseActivity {
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
        if (mIsAdd) {

        } else {
            fillView();
        }

    }

    @OnClick(R.id.rule_setting)
    public void onClickRuleSetting(){
        if (!TextUtils.isEmpty(mPassword.getText().toString())){
            Intent intent = new Intent(this,RuleSettingActivity.class);
            intent.putExtra("password",mPassword.getText().toString());
            startActivity(intent);
        }

//        new MaterialDialog.Builder(PasswordDetailActivity.this)
//                .title("默认规则")
//                .items(R.array.password_default_rule)
//                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
//                    @Override
//                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
//                        onRuleSelected(which);
//                        return false;
//                    }
//                }).show();
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

}
