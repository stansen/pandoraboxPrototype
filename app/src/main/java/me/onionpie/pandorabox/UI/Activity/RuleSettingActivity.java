package me.onionpie.pandorabox.UI.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.UI.Adapter.RuleSettingGridAdapter;

public class RuleSettingActivity extends BaseActivity {
    @Bind(R.id.rule_grid)
    RecyclerView mRecyclerView;
    private String mRealPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_setting);
        ButterKnife.bind(this);
        getIntentData();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(RuleSettingActivity.this,6);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(new RuleSettingGridAdapter(mRealPassword.toCharArray()));
    }
    private void getIntentData(){
        mRealPassword = getIntent().getStringExtra("password");
    }
}
