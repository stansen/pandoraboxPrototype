package me.onionpie.pandorabox.UI.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.Widget.TopBarFragment;

public class ExportActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        addFragment(R.id.top_bar, TopBarFragment.newInstance("导出密码",""));
    }



}
