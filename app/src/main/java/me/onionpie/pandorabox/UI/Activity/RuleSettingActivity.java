package me.onionpie.pandorabox.UI.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.onionpie.pandorabox.Helper.ApiLevelHelper;
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
    }



    @Override
    public void onItemClicked(View view, int position) {
        new MaterialDialog.Builder(RuleSettingActivity.this)
                .title("默认规则")
                .items(R.array.password_default_rule)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        onRuleSelected(which, text);
                        return true;
                    }
                }).show();
    }

    private void onRuleSelected(int which, CharSequence text) {

        switch (which) {
            case 0:
                showToast(String.valueOf(text));
                break;
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
    }

}
