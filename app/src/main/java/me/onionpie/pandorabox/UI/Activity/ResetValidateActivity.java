package me.onionpie.pandorabox.UI.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.OnClick;
import me.onionpie.pandorabox.ConstansParam.Constans;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.Rx.Event.UpdateValidateConfigEvent;
import me.onionpie.pandorabox.Rx.RxBus;
import me.onionpie.pandorabox.Utils.CommonPreference;
import me.onionpie.pandorabox.Widget.TopBarFragment;

public class ResetValidateActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_validate);
        addFragment(R.id.top_bar, TopBarFragment.newInstance("重置设置", ""));
    }

    @OnClick(R.id.reset)
    public void onClickToReset() {
        CommonPreference.putBoolean(this, Constans.KEY_SET_SCAN_CODE_VALIDATE, false);
        CommonPreference.putBoolean(this, Constans.KEY_SET_SINGLE_PASSSWORD_VALIDATE, false);
        if (RxBus.getInstance().hasObservers()) {
            RxBus.getInstance().send(new UpdateValidateConfigEvent());
        }
        onBackPressed();
    }
}
