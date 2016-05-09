package me.onionpie.pandorabox.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jiudeng007.barcodelib.ScanCodeActivity;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.onionpie.pandorabox.ConstansParam.Constans;
import me.onionpie.pandorabox.Helper.DoubleClickExitHelper;
import me.onionpie.pandorabox.Model.PasswordTextInfoModel;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.UI.Fragment.PasswordListFragment;
import me.onionpie.pandorabox.UI.Fragment.PasswordListFragment.OnListFragmentInteractionListener;
import me.onionpie.pandorabox.UI.Fragment.PasswordRuleFragment;
import me.onionpie.pandorabox.Utils.AppManager;
import me.onionpie.pandorabox.Utils.CommonPreference;
import rx.functions.Action1;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnListFragmentInteractionListener {
    private static final int OPENPASSWORDDETAIL = 10000;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.nav_view)
    NavigationView mNavView;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    DoubleClickExitHelper mDoubleClickExitHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mDoubleClickExitHelper = new DoubleClickExitHelper(this);
//        StatusBarCompat.compat(this, getResources().getColor(R.color.colorPrimary));
        RxView.clicks(mFab).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new MaterialDialog.Builder(HomeActivity.this)
                                .title("请选择记录方式")
                                .negativeText("拍照记录")
                                .positiveText("文字记录")
                                .neutralText("取消")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();

                                        Intent intent = PasswordDetailActivity.getIntent(HomeActivity.this,
                                                true);
                                        startActivity(intent);
                                    }
                                })
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                    }
                                })
                                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();

                                    }
                                }).show();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                    }
                });
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mNavView.setNavigationItemSelectedListener(this);
        mNavView.setCheckedItem(R.id.password_list);
        replaceFragment(R.id.main_content_container, new PasswordListFragment());
        mToolbar.setTitle(getString(R.string.password_list));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        AppManager.getAppManager().AppExit(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        if (id == R.id.nav_scan_code || id == R.id.nav_send) {

        } else
            mToolbar.setTitle(item.getTitle());
        if (id == R.id.password_list) {
            replaceFragment(R.id.main_content_container, new PasswordListFragment());
            // Handle the camera action
        } else if (id == R.id.password_rule_setting) {
            replaceFragment(R.id.main_content_container, new PasswordRuleFragment());
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_scan_code) {
            Intent intent = new Intent(this, ScanCodeActivity.class);
            startActivity(intent);
        } else if (id == R.id.password_export) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(PasswordTextInfoModel item,int position) {
        Intent intent = PasswordDetailActivity.getStartIntent(this,false,item,position);
        startActivityForResult(intent,OPENPASSWORDDETAIL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case OPENPASSWORDDETAIL:
                    break;
            }
        }
    }

    private void validate2DCode(){
        if (CommonPreference.getBoolean(this, Constans.QRCODE_VALID)){

        }else {

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 是否退出应用
            return mDoubleClickExitHelper.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }
}
