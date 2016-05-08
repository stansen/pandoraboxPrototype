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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import me.onionpie.pandorabox.Utils.AppManager;

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

    /**
     * @param context
     * @param realPassword
     * @param title
     * @param singleCharPasswordRuleModels
     * @return
     */
    public static Intent getStartIntent(Context context, String realPassword
            , String title, ArrayList<SingleCharPasswordRuleModel> singleCharPasswordRuleModels) {
        Intent starter = new Intent(context, RuleSettingActivity.class);
        starter.putExtra("password", realPassword);
        starter.putExtra("title", title);
        starter.putParcelableArrayListExtra("rule_password_array", singleCharPasswordRuleModels);
        return starter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_setting);
        ButterKnife.bind(this);
        getIntentData();
        initView();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(RuleSettingActivity.this, 6);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(new RuleSettingGridAdapter(mSingleCharPasswordRuleModels, this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCollapsingToolbarLayout.setTitle(mTitleName);
    }

    private void getIntentData() {
        mRealPassword = getIntent().getStringExtra("password");
        mTitleName = getIntent().getStringExtra("title");
        mSingleCharPasswordRuleModels = getIntent().getParcelableArrayListExtra("rule_password_array");
        if (mSingleCharPasswordRuleModels.size() == 0)
            initToStart();
    }

    private void initToStart() {
        mSingleCharPasswordRuleModels.clear();
        char[] chars = mRealPassword.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            SingleCharPasswordRuleModel ruleModel = new SingleCharPasswordRuleModel();
            ruleModel.mRuleId = i;
            ruleModel.mTargetChar = String.valueOf(chars[i]);
            ruleModel.mDestinyChar = ruleModel.mTargetChar;
            mSingleCharPasswordRuleModels.add(ruleModel);
            Log.d("target", ruleModel.mTargetChar);
        }
    }

    @OnClick(R.id.confirm_save)
    public void confirmSave() {
//        showCenterToast("保存成功");
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("rule_password_array", mSingleCharPasswordRuleModels);
        setResult(RESULT_OK, intent);
        AppManager.getAppManager().finishActivity();

    }

    @Override
    public void onItemClicked(final int position) {
        new MaterialDialog.Builder(RuleSettingActivity.this)
                .title("默认规则")
                .items(R.array.password_default_rule)
                .itemsCallbackSingleChoice(mSingleCharPasswordRuleModels.get(position).mSelectedPosition, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        onRuleSelected(which, text, position);
                        return true;
                    }
                }).show();
    }

    private void onRuleSelected(int which, CharSequence ruleName, int position) {
        Log.d("target_position", mSingleCharPasswordRuleModels.get(position).mSelectedPosition + "");
//        showToast(String.valueOf(ruleName));
        mSingleCharPasswordRuleModels.get(position).mRuleName = String.valueOf(ruleName);
        switch (which) {
            case 0:
                mSingleCharPasswordRuleModels.get(position).mIsRuleSetted = true;
                mSingleCharPasswordRuleModels.get(position).mSelectedPosition = which;
                mSingleCharPasswordRuleModels.get(position).mDestinyChar = "*";
                break;
            case 1:
                replaceDialog(mSingleCharPasswordRuleModels.get(position).mTargetChar, position, which);
                Log.d("target_position", mSingleCharPasswordRuleModels.get(position).mSelectedPosition + "");
                break;
            case 2:
                if (mSingleCharPasswordRuleModels.get(position).mSelectedPosition == which) {
                    showToast("请先取消再交换");
                } else
                    exchangeDialog(mSingleCharPasswordRuleModels.get(position).mTargetChar, position, which);
                break;
            case 3:
                char[] chars = mRealPassword.toCharArray();
                if (mSingleCharPasswordRuleModels.get(position).mSelectedPosition == 2) {
                    int exchangePosition = mSingleCharPasswordRuleModels.get(position).mExchangePosition;
                    SingleCharPasswordRuleModel singleCharPasswordRuleModel = new SingleCharPasswordRuleModel(position, String.valueOf(chars[position]));
                    mSingleCharPasswordRuleModels.set(position, singleCharPasswordRuleModel);

                    SingleCharPasswordRuleModel singleCharPasswordRuleModel1 = new SingleCharPasswordRuleModel(exchangePosition, String.valueOf(chars[exchangePosition]));
                    mSingleCharPasswordRuleModels.set(exchangePosition, singleCharPasswordRuleModel1);
                } else {
                    SingleCharPasswordRuleModel singleCharPasswordRuleModel = new SingleCharPasswordRuleModel(position, String.valueOf(chars[position]));
                    mSingleCharPasswordRuleModels.set(position, singleCharPasswordRuleModel);
                }

                break;
            default:
                break;
        }
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    private void replaceDialog(String target, final int position, final int whichSelected) {
        final String[] destiny = new String[1];
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("替换").positiveText("确定").negativeText("取消")
                .customView(R.layout.repalce_dialog_layout, false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mSingleCharPasswordRuleModels.get(position).mSelectedPosition = whichSelected;
                        mSingleCharPasswordRuleModels.get(position).mIsRuleSetted = true;
                        mSingleCharPasswordRuleModels.get(position).mDestinyChar = destiny[0];
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                })
                .show();
        View view = dialog.getCustomView();
        TextView targetCharTV = (TextView) view.findViewById(R.id.target_char);
        final EditText destinyCharEt = (EditText) view.findViewById(R.id.destiny_char);
        targetCharTV.setText(String.format("将       %s", target));
        destinyCharEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 1)
                    s.delete(1, s.toString().length());
                destinyCharEt.setSelection(s.toString().length());
                destiny[0] = destinyCharEt.getText().toString();
            }
        });
    }

    private void exchangeDialog(final String target, final int position, final int whichSelected) {
        final String[] destiny = new String[1];
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("交换").positiveText("确定").negativeText("取消")
                .customView(R.layout.exchange_dialog_layout, false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        textView.setText(destiny[0]);
                        int destinyPosition;
                        mSingleCharPasswordRuleModels.get(position).mIsRuleSetted = true;
                        destinyPosition = Integer.valueOf(destiny[0]) - 1;
                        Log.d("position", destinyPosition + "");
                        String temp = "";
                        temp = mSingleCharPasswordRuleModels.get(position).mDestinyChar;
                        mSingleCharPasswordRuleModels.get(position).mDestinyChar = mSingleCharPasswordRuleModels.get(destinyPosition).mDestinyChar;
                        mSingleCharPasswordRuleModels.get(position).mExchangePosition = destinyPosition;
                        mSingleCharPasswordRuleModels.get(position).mSelectedPosition = whichSelected;
                        mSingleCharPasswordRuleModels.get(destinyPosition).mIsRuleSetted = true;
                        mSingleCharPasswordRuleModels.get(destinyPosition).mSelectedPosition = whichSelected;
                        mSingleCharPasswordRuleModels.get(destinyPosition).mDestinyChar = temp;
                        mSingleCharPasswordRuleModels.get(destinyPosition).mExchangePosition = position;
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                })
                .show();
        View view = dialog.getView();
        TextView targetCharTV = (TextView) view.findViewById(R.id.target_char);
        final EditText destinyPositionCharEt = (EditText) view.findViewById(R.id.destiny_exchange_num);
        Log.d("exchange", target);
        targetCharTV.setText("将" + target + "与位置为");
        destinyPositionCharEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (s.toString().startsWith("0")) {
                        s.delete(0, 1);
                    }
                }
                destiny[0] = s.toString();
            }
        });

    }

}
