package me.onionpie.pandorabox.UI.Activity;

import android.os.Bundle;

import me.onionpie.pandorabox.ConstansParam.Constans;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.Utils.CommonPreference;
import me.onionpie.pandorabox.Widget.TopBarFragment;

public class PasswordDetailActivity extends BaseActivity {
    private boolean mIsAdd = true;
    private static final String IS_ADD = "is_add";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_detail);
        replaceFragment(R.id.top_bar, TopBarFragment.newInstance("密码详情", ""));
        getData();
        if (mIsAdd){

        }else {
            fillView();
        }
    }

    private void getData(){
        mIsAdd = getIntent().getBooleanExtra(IS_ADD,true);
    }

    private void fillView(){

    }

}
