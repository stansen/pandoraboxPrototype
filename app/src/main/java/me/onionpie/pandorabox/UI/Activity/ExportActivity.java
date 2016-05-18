package me.onionpie.pandorabox.UI.Activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.FrameLayout;

import com.dd.CircularProgressButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.Utils.StatusBarCompat;
import me.onionpie.pandorabox.Widget.TopBarFragment;

public class ExportActivity extends BaseCameraActivity {

    @Bind(R.id.top_bar)
    FrameLayout mTopBar;
    @Bind(R.id.btnWithText)
    CircularProgressButton mBtnWithText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        ButterKnife.bind(this);
        mType = 3;
        addFragment(R.id.top_bar, TopBarFragment.newInstance("导出密码", ""));
        StatusBarCompat.compat(this, ContextCompat.getColor(getApplicationContext(), R.color.light_blue));
        setResult();
    }
    @OnClick(R.id.btnWithText)
    public void onClickBtn(){
        validatePasswordAndScanCode();
    }
    private void setResult() {
        mBaseInterface = new BaseInterface() {
            @Override
            void validateSuccess() {
                showToast("导出成功");
            }

            @Override
            void validateFailed() {
                showToast("验证失败");
            }
        };
    }
}
