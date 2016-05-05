package me.onionpie.pandorabox.UI.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.onionpie.pandorabox.Model.SingleCharPasswordRuleModel;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.UI.Adapter.RuleSettingGridAdapter;

public class RuleSettingActivity extends BaseActivity implements RuleSettingGridAdapter.onGridItemClickListener {
    @Bind(R.id.rule_grid)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    //    @Bind(R.id.category_title)
//    TextView mTitle;
//    @Bind(R.id.back)
//    ImageButton mToolbarBack;
    private String mRealPassword;
    private String mTitleName;
    private ArrayList<SingleCharPasswordRuleModel> mSingleCharPasswordRuleModels = new ArrayList<>();

    public static Intent getStartIntent(Context context, String realPassword, String title) {
        Intent starter = new Intent(context, RuleSettingActivity.class);
        starter.putExtra("password", realPassword);
        starter.putExtra("title", title);
        return starter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_setting);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        getIntentData();
        initView();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(RuleSettingActivity.this, 6);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(new RuleSettingGridAdapter(mRealPassword.toCharArray(), this));
    }

    private void initView() {

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCollapsingToolbarLayout.setTitle(mTitleName);
    }

    private void getIntentData() {
        mRealPassword = getIntent().getStringExtra("password");
        mTitleName = getIntent().getStringExtra("title");
        char[] chars = mTitleName.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            SingleCharPasswordRuleModel ruleModel = new SingleCharPasswordRuleModel();
            ruleModel.mRuleId = i;
            ruleModel.mTargetChar = String.valueOf(chars[i]);
            mSingleCharPasswordRuleModels.add(ruleModel);
        }
    }

    @OnClick(R.id.confirm_save)
    public void confirmSave() {
        showCenterToast("保存成功");
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("rule_password_array", mSingleCharPasswordRuleModels);
        setIntent(intent);
    }

    @Override
    public void onItemClicked(final View view, int position) {
        new MaterialDialog.Builder(RuleSettingActivity.this)
                .title("默认规则")
                .items(R.array.password_default_rule)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        onRuleSelected(view, which, text);
                        return true;
                    }
                }).show();
    }

    private void onRuleSelected(View view, int which, CharSequence text) {
        view.setVisibility(View.VISIBLE);
        mSingleCharPasswordRuleModels.get(which).mIsRuleSetted = true;
        showToast(String.valueOf(text));
        String[] ruleNames = getResources().getStringArray(R.array.password_default_rule);
        for (String name : ruleNames) {
            mSingleCharPasswordRuleModels.get(which).mRuleName = name;
        }
        switch (which) {
            case 0:

                break;
            case 1:
                replaceDialog(String.valueOf(text));
                break;
            case 2:
                break;
            case 3:
                mSingleCharPasswordRuleModels.get(which).mIsRuleSetted = false;
                view.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private void replaceDialog(String target) {
       MaterialDialog dialog =  new MaterialDialog.Builder(this)
                .title("替换").positiveText("确定").negativeText("取消")
                .customView(R.layout.repalce_dialog_layout,false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();
        View view = dialog.getCustomView();
        TextView targetCharET = (TextView) view.findViewById(R.id.target_char);
        EditText destinyCharEt = (EditText)view.findViewById(R.id.destiny_char);
        targetCharET.setText(target);
    }

}
