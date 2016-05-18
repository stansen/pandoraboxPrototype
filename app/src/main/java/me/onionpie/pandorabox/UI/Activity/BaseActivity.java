package me.onionpie.pandorabox.UI.Activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.Utils.AppManager;
import me.onionpie.pandorabox.Utils.CustomToast;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class BaseActivity extends AppCompatActivity {
    private MaterialDialog mMaterialDialog;
    private CompositeSubscription mCompositeSubscription;
//    Navigator mNavigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
//        setOrientation();
        mCompositeSubscription = new CompositeSubscription();
        AppManager.getAppManager().addActivity(this);
    }
    private void setStatusBarColor(){
        if (Build.VERSION.SDK_INT >=21){

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (JdApplication.sUserInfo == null && isLogin()) {
//            Log.d("test", "get user info");
////            getUserInfo();
//            ((JdApplication) getApplication()).getUserInfo();
//        }
    }

    public String getCurrentClassName() {
        return getClass().getName();
    }

    protected void setClickListener(View view){
        setClickListener(view, 1000);
    }

    protected void setClickListener(final View view, int delay){

        addSubscription(RxView.clicks(view)
                .throttleFirst(delay, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        click(view);
                    }
                }));
    }

    protected void click(View view){

    }

    public void addSubscription(Subscription s) {
        mCompositeSubscription.add(s);
    }

    public CompositeSubscription getSubscription(){
        return mCompositeSubscription;
    }


    @Override
    public void onBackPressed() {
        AppManager.getAppManager().finishActivity(this);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription != null) {
            mCompositeSubscription.clear();
            mCompositeSubscription = null;
        }
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 设置屏幕方向
     */
//    protected void setOrientation() {
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//    }

    public void showToast(String content) {
        if (!content.isEmpty())
        CustomToast.getCustomedToast().showToast(getApplicationContext(), content);
    }

    public void showCenterToast(String content) {
        CustomToast.getCustomedToast().showCenterToast(getApplicationContext(), content);
    }

    public void showSnackbar(String content) {

    }

    /**
     * 添加fragment
     *
     * @param containerViewId fragment容器的id
     * @param fragment        使用的fragment
     */
    protected void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }
    /**
     * 添加带tag的fragment
     *
     * @param containerViewId
     * @param fragment
     * @param tag
     */
    protected void addFragmentWithTag(int containerViewId, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.add(containerViewId, fragment, tag);
        fragmentTransaction.commit();
    }

    protected void removeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }

    protected void replaceFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    public void showLoadDialog(String title, String content) {
        if (mMaterialDialog == null) {
            mMaterialDialog = new MaterialDialog.Builder(this)
                    .title(title)
                    .content(content)
                    .progress(true, 0)
                    .show();
            mMaterialDialog.setCancelable(false);
        } else {
            mMaterialDialog.setTitle(title);
            mMaterialDialog.setContent(content);
            mMaterialDialog.show();
        }
    }

    public void hideLoadDialog() {
        if (mMaterialDialog != null)
            mMaterialDialog.dismiss();
        else
            Log.e("dialog", "materialDialog is null");
    }

    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public boolean isKeyBoardOpened() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen = imm.isActive();//isOpen若返回true，则表示输入法打开
        return isOpen;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
