package me.onionpie.pandorabox.UI.Activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.onionpie.pandorabox.Helper.ApiLevelHelper;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.UI.Adapter.RuleSettingGridAdapter;
import me.onionpie.pandorabox.Widget.TextSharedElementCallback;

public class RuleSettingActivity extends BaseActivity implements RuleSettingGridAdapter.onGridItemClickListener {
    @Bind(R.id.rule_grid)
    RecyclerView mRecyclerView;
    @Bind(R.id.category_title)
    TextView mTitle;
    @Bind(R.id.back)
    ImageButton mToolbarBack;
    private String mRealPassword;
    private String mTitleName;
    public static Intent getStartIntent(Context context, String realPassword,String title) {
        Intent starter = new Intent(context, RuleSettingActivity.class);
        starter.putExtra("password", realPassword);
        starter.putExtra("title",title);
        return starter;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_setting);
        ButterKnife.bind(this);
        int a = 0;
        initView();
        getIntentData();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(RuleSettingActivity.this, 6);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(new RuleSettingGridAdapter(mRealPassword.toCharArray(), this));
    }
    private void initView(){
        int categoryNameTextSize = getResources()
                .getDimensionPixelSize(R.dimen.category_item_text_size);
        int paddingStart = getResources().getDimensionPixelSize(R.dimen.spacing_double);
        final int startDelay = getResources().getInteger(R.integer.toolbar_transition_duration);
        ActivityCompat.setEnterSharedElementCallback(this,
                new TextSharedElementCallback(categoryNameTextSize, paddingStart) {
                    @Override
                    public void onSharedElementStart(List<String> sharedElementNames,
                                                     List<View> sharedElements,
                                                     List<View> sharedElementSnapshots) {
                        super.onSharedElementStart(sharedElementNames,
                                sharedElements,
                                sharedElementSnapshots);
                        mToolbarBack.setScaleX(0f);
                        mToolbarBack.setScaleY(0f);
                    }

                    @Override
                    public void onSharedElementEnd(List<String> sharedElementNames,
                                                   List<View> sharedElements,
                                                   List<View> sharedElementSnapshots) {
                        super.onSharedElementEnd(sharedElementNames,
                                sharedElements,
                                sharedElementSnapshots);
                        // Make sure to perform this animation after the transition has ended.
                        ViewCompat.animate(mToolbarBack)
                                .setStartDelay(startDelay)
                                .scaleX(1f)
                                .scaleY(1f)
                                .alpha(1f);
                    }
                });
        ViewCompat.animate(mToolbarBack)
                .scaleX(1)
                .scaleY(1)
                .alpha(1)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setStartDelay(300)
                .start();
        mTitle.setText(mTitleName);
    }
    private void getIntentData() {
        mRealPassword = getIntent().getStringExtra("password");
        mTitleName = getIntent().getStringExtra("title");
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        ViewCompat.animate(mToolbarBack)
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0f)
                .setDuration(100)
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {

                    }
                    @SuppressLint("NewApi")
                    @Override
                    public void onAnimationEnd(View view) {
                        if (isFinishing() ||
                                (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.JELLY_BEAN_MR1)
                                        && isDestroyed())) {
                            return;
                        }
                        RuleSettingActivity.super.onBackPressed();
                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }
                })
                .start();
    }
    @OnClick(R.id.back)
    public void back(){
        onBackPressed();
    }
    @Override
    public void onItemClicked(View view,int position) {
        new MaterialDialog.Builder(RuleSettingActivity.this)
                .title("默认规则")
                .items(R.array.password_default_rule)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        onRuleSelected(which,text);
                        return true;
                    }
                }).show();
    }

    private void onRuleSelected(int which,CharSequence text) {

        switch (which){
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
