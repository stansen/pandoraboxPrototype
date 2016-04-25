package me.onionpie.pandorabox.UI.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import me.onionpie.pandorabox.PandoraApplication;
import me.onionpie.pandorabox.Utils.CustomToast;


/**
 * Created by Gstansen on 2015/11/26.
 */
public abstract class BaseFragment extends Fragment {
    private MaterialDialog mMaterialDialog;
    private boolean needCancel = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 设置是否需要自动取消okhttp连接
     * @param needCancel
     */
    protected void setNeedCancel(boolean needCancel) {
        this.needCancel = needCancel;
    }

    public Object getCurrentName(){
        return getClass().getName();
    }

    public void showLoadDialog(String title, String content) {
        if (mMaterialDialog == null) {
            mMaterialDialog = new MaterialDialog.Builder(getActivity())
                    .title(title)
                    .content(content)
                    .progress(true, 0)
                    .show();
            mMaterialDialog.setCancelable(false);
        } else {
            mMaterialDialog.setTitle("Dialog");
            mMaterialDialog.setContent("wait");
            mMaterialDialog.show();
        }
    }
    protected void addFragmentWithTag(int containerViewId, Fragment fragment,String tag) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.add(containerViewId, fragment,tag);
        fragmentTransaction.commit();
    }




    protected void addFragment(int containerViewId, Fragment fragment){
        getChildFragmentManager().beginTransaction().add(containerViewId, fragment).commit();
    }

    protected void replaceFragment(int containerViewId, Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.commit();
    }
    protected void replaceFragmentWithTag(int containerViewId, Fragment fragment,String tag){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.replace(containerViewId,fragment,tag);
        fragmentTransaction.commit();
    }

    protected void addFragmentTagChild(int containerViewId, Fragment fragment,String tag) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
//        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.add(containerViewId, fragment, tag);
        fragmentTransaction.commit();
    }
    protected void replaceFragmentTagChild(int containerViewId, Fragment fragment,String tag){
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
//        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.replace(containerViewId, fragment, tag);
        fragmentTransaction.commit();
    }
    public void showToast(String content) {
        CustomToast.getCustomedToast().showToast(PandoraApplication.getmApplicationContext(), content);
    }
    public void showToastLengthLong(String content){
        Toast.makeText(PandoraApplication.getmApplicationContext(),content,Toast.LENGTH_LONG).show();
    }

    public void hideLoadDialog() {
        if (mMaterialDialog != null){
            mMaterialDialog.dismiss();
            mMaterialDialog = null;
        }

        else
            Log.e("dialog", "materialDialog is null");
    }


    public void clearLoginState() {
//        UserHelper.clearUserInfo(getContext());
//        JdApplication.sUserInfo = null;
    }


    /** Fragment当前状态是否可见 */
    protected boolean isVisible;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    /**
     * 可见
     */
    protected void onVisible() {
        delayLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {


    }


    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected void delayLoad(){

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (needCancel){
//            OkHttpUtil.getInstance().cancelTag(getCurrentName());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        RefWatcher refWatcher = JdApplication.getRefWatcher(getActivity());
//        refWatcher.watch(this);
    }
}
