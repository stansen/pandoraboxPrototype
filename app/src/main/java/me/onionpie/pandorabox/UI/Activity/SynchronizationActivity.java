package me.onionpie.pandorabox.UI.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dd.CircularProgressButton;
import com.example.jiudeng007.barcodelib.SheetDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.Utils.StatusBarCompat;
import me.onionpie.pandorabox.Widget.TopBarFragment;

public class SynchronizationActivity extends BaseCameraActivity {

    @Bind(R.id.top_bar)
    FrameLayout mTopBar;
    @Bind(R.id.user_icon)
    ImageView mUserIcon;
    @Bind(R.id.btnWithText)
    CircularProgressButton mBtnWithText;
    @Bind(R.id.scan_code_select)
    Button mScanCodeSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronization);
        StatusBarCompat.compat(this, getResources().getColor(R.color.light_blue));
        ButterKnife.bind(this);
        addFragment(R.id.top_bar, TopBarFragment.newInstance("同步数据", ""));
        mBtnWithText.setIndeterminateProgressMode(true);
        mType = 2;
        setResult();
    }

    @OnClick(R.id.btnWithText)
    public void onClickCir() {
        if (mBtnWithText.getProgress() == 0) {
            mBtnWithText.setProgress(50);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBtnWithText.setProgress(100);
//                    mBtnWithText.setProgress(0);
                }
            }, 2000);
        } else if (mBtnWithText.getProgress() == -1) {
            mBtnWithText.setProgress(0);
        } else {
            mBtnWithText.setProgress(-1);
        }

    }
    private void setResult(){
        mBaseInterface = new BaseInterface() {
            @Override
            void validateSuccess() {
                showLoadDialog("上传中","");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadDialog();
                        showToast("上传成功");
                    }
                },1500);
            }

            @Override
            void validateFailed() {
                showToast("上传失败");
            }
        };
    }
    @OnClick(R.id.scan_code_select)
    public void onClickScanCode() {
        showSheetDialog();
    }
}
