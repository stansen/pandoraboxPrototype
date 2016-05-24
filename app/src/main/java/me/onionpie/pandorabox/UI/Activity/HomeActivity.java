package me.onionpie.pandorabox.UI.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jiudeng007.barcodelib.QRHelper;
import com.example.jiudeng007.barcodelib.ScanActivity;
import com.example.jiudeng007.barcodelib.SheetDialog;
import com.example.jiudeng007.barcodelib.Utils;
import com.google.zxing.Result;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.onionpie.pandorabox.ConstansParam.Constans;
import me.onionpie.pandorabox.Helper.DoubleClickExitHelper;
import me.onionpie.pandorabox.Model.PasswordTextInfoModel;
import me.onionpie.pandorabox.PandoraApplication;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.UI.Fragment.CodeGenerateFragment;
import me.onionpie.pandorabox.UI.Fragment.PasswordListFragment;
import me.onionpie.pandorabox.UI.Fragment.PasswordListFragment.OnListFragmentInteractionListener;
import me.onionpie.pandorabox.UI.Fragment.ValidateRuleFragment;
import me.onionpie.pandorabox.Utils.AppManager;
import me.onionpie.pandorabox.Utils.CommonPreference;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnListFragmentInteractionListener, SheetDialog.onDialogItemClickListener {
    private static final int OPENPASSWORDDETAIL = 10000;
    private static final int CameraRequest = 10002;
    private static final int GalleryRequest = 10003;
    public static final int RequestCode = 10001;
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

//        startService();
        mDoubleClickExitHelper = new DoubleClickExitHelper(this);
//        StatusBarCompat.compat(this, getResources().getColor(R.color.colorPrimary));
        RxView.clicks(mFab).throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showChooeseDialog();
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
        setNavViewHeader();
//        TextView textView = new TextView(this);
//        textView.setText("注销");
//        mDrawerLayout.addView(textView,-1,mDrawerLayout.getLayoutParams());
//        if (CommonPreference.getBoolean(this,Constans.KEY_IS_USER_LOGIN)){
//
//        }
    }

    private void showChooeseDialog() {
        Intent intent = PasswordDetailActivity.getIntent(HomeActivity.this,
                true);
        startActivity(intent);
//        new MaterialDialog.Builder(HomeActivity.this)
//                .title("请选择记录方式")
//                .positiveText("文字记录")
//                .neutralText("取消")
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        dialog.dismiss();
//
//                        Intent intent = PasswordDetailActivity.getIntent(HomeActivity.this,
//                                true);
//                        startActivity(intent);
//                    }
//                })
//                .onNeutral(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        dialog.dismiss();
//
//                    }
//                }).show();
    }

    private void setNavViewHeader() {
        View view = mNavView.getHeaderView(0);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.container);
        final TextView textView = (TextView) view.findViewById(R.id.title);
        textView.setText(PandoraApplication.phone);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonPreference.getBoolean(HomeActivity.this, Constans.KEY_IS_USER_LOGIN)) {
                    //登录
                    Log.v("is_login", "true");
                } else {
                    //未登录
                    Log.v("is_login", "false");
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
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
        if (CommonPreference.getBoolean(this, Constans.KEY_IS_USER_LOGIN)){
            getMenuInflater().inflate(R.menu.home, menu);
            return true;
        }else return false;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.out_of_login) {
            CommonPreference.putBoolean(this,Constans.KEY_IS_USER_LOGIN,false);
//            onCreateOptionsMenu(null);
            item.setVisible(false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        if (id == R.id.nav_synchronization || id == R.id.nav_send) {

        } else
            mToolbar.setTitle(item.getTitle());
        if (id == R.id.password_list) {
            replaceFragment(R.id.main_content_container, new PasswordListFragment());
            // Handle the camera action
        } else if (id == R.id.password_rule_setting) {
            replaceFragment(R.id.main_content_container, new ValidateRuleFragment());
        } else if (id == R.id.generate_code) {
            replaceFragment(R.id.main_content_container, new CodeGenerateFragment());
        } else if (id == R.id.nav_synchronization) {
//            Intent intent = new Intent(this, SynchronizationActivity.class);
//            startActivity(intent);
            if (CommonPreference.getBoolean(this,Constans.KEY_IS_USER_LOGIN)){
                Intent intent = new Intent(this, SynchronizationActivity.class);
                startActivity(intent);
            }else {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.password_export) {
            Intent intent = new Intent(this, ExportActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private PasswordTextInfoModel mPasswordTextInfoModel;
    private int mPosition;

    @Override
    public void onListFragmentInteraction(PasswordTextInfoModel item, int position) {

        mPasswordTextInfoModel = item;
        mPosition = position;
        validatePasswordAndScanCode();
//        boolean isScanCodeUseful = CommonPreference.getBoolean(this, Constans.KEY_IS_SCAN_CODE_USEFUL);
//        if (isScanCodeUseful) {
//            Intent intent = PasswordDetailActivity.getStartIntent(this, false, item, position);
//            startActivityForResult(intent, OPENPASSWORDDETAIL);
//        } else {
//            showSheetDialog();
//        }

    }

    private void validatePasswordAndScanCode() {
        if (CommonPreference.getBoolean(getApplicationContext(), Constans.KEY_SET_SCAN_CODE_VALIDATE)
                && CommonPreference.getBoolean(getApplicationContext(), Constans.KEY_SET_SINGLE_PASSSWORD_VALIDATE)) {
            showPasswordDialog();
        } else if (CommonPreference.getBoolean(getApplicationContext(), Constans.KEY_SET_SCAN_CODE_VALIDATE)) {
            validateScanCode();
//            showSheetDialog();
        } else if (CommonPreference.getBoolean(getApplicationContext(), Constans.KEY_SET_SINGLE_PASSSWORD_VALIDATE)) {
            showPasswordDialog();
        } else {
            Intent intent = PasswordDetailActivity.getStartIntent(this, false, mPasswordTextInfoModel, mPosition);
            startActivityForResult(intent, OPENPASSWORDDETAIL);
        }
    }

    private void validateScanCode() {
        boolean isScanCodeUseful = CommonPreference.getBoolean(this, Constans.KEY_IS_SCAN_CODE_USEFUL);
        if (isScanCodeUseful) {
            Intent intent = PasswordDetailActivity.getStartIntent(this, false, mPasswordTextInfoModel, mPosition);
            startActivityForResult(intent, OPENPASSWORDDETAIL);
        } else {
            showSheetDialog();
        }
    }

    private void showPasswordDialog() {
        new MaterialDialog.Builder(this)
                .title("验证密码")
                .content("请输入验证的密码")
                .inputType(InputType.TYPE_TEXT_VARIATION_PASSWORD |
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .inputRange(4, 16)
                .positiveText("确定")
                .input("", "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        String password = input.toString();
                        if (password.equals(CommonPreference.getString(getApplicationContext(), Constans.KEY_PASSWORD_VALUE))) {
                            if (CommonPreference.getBoolean(getApplicationContext(), Constans.KEY_SET_SCAN_CODE_VALIDATE))
                                validateScanCode();
                            else {
                                Intent intent = PasswordDetailActivity.getStartIntent(HomeActivity.this, false, mPasswordTextInfoModel, mPosition);
                                startActivityForResult(intent, OPENPASSWORDDETAIL);
                            }
                        } else
                            showToast("验证失败");
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case OPENPASSWORDDETAIL:
                    break;
                case RequestCode:
                    String[] proj = {MediaStore.Images.Media.DATA};
                    // 获取选中图片的路径
                    Cursor cursor = getContentResolver().query(data.getData(),
                            proj, null, null, null);

                    if (cursor.moveToFirst()) {

                        int column_index = cursor
                                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        String photo_path = cursor.getString(column_index);
                        if (photo_path == null) {
                            photo_path = Utils.getPath(getApplicationContext(),
                                    data.getData());
                            Log.i("123path  Utils", photo_path);
                        }
                        Log.i("123path", photo_path);
                        final String finalPhoto_path = photo_path;
                        Subscription subscription = Observable.create(new Observable.OnSubscribe<Boolean>() {
                            @Override
                            public void call(Subscriber<? super Boolean> subscriber) {
                                Result result = QRHelper.getScanBitmap(finalPhoto_path);
                                // String result = decode(photo_path);
                                if (result == null) {
                                    subscriber.onNext(false);
                                } else {
                                    Log.i("123result", result.toString());
                                    // Log.i("123result", result.getText());
                                    // 数据返回
                                    String recode = QRHelper.recode(result.toString());
                                    try {
//                                        String destiny = Sercurity.aesEncrypt(recode, PandoraApplication.scanCodeAk);
                                        if (recode.equals(CommonPreference.getString(HomeActivity.this, Constans.KEY_SCAN_CODE_VALUE))) {
                                            subscriber.onNext(true);
                                        } else {
                                            subscriber.onNext(false);
                                        }
                                    } catch (Exception e) {
                                        subscriber.onNext(false);
                                        e.printStackTrace();
                                    }
                                    Log.d("recode", recode);
                                }
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<Boolean>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(getApplicationContext(), "验证失败", Toast.LENGTH_SHORT)
                                                .show();
                                    }

                                    @Override
                                    public void onNext(Boolean aBoolean) {
                                        if (aBoolean) {
                                            CommonPreference.putBoolean(getApplicationContext(), Constans.KEY_IS_SCAN_CODE_USEFUL, true);
                                            Intent intent = PasswordDetailActivity.getStartIntent(HomeActivity.this, false, mPasswordTextInfoModel, mPosition);
                                            startActivityForResult(intent, OPENPASSWORDDETAIL);
                                        } else {
                                            Toast.makeText(getApplicationContext(), "验证失败", Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    }
                                });
                        addSubscription(subscription);
                    }
                    cursor.close();
            }
        }
    }

    private void showSheetDialog() {
        SheetDialog sheetDialog = new SheetDialog();
        sheetDialog.setOnDialogItemClickListener(HomeActivity.this);
        sheetDialog.showDialog(HomeActivity.this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 是否退出应用
            return mDoubleClickExitHelper.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CameraRequest:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Intent intent = new Intent(this, ScanActivity.class);
                    startActivity(intent);
//                    getImageFromGallery();
                } else {
                    // Permission Denied
                    Toast.makeText(HomeActivity.this, "拍照请求被拒绝了", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case GalleryRequest:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    getImageFromGallery();
                } else {
                    // Permission Denied
                    Toast.makeText(HomeActivity.this, "相册请求被拒绝了", Toast.LENGTH_SHORT)
                            .show();
                }

                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onItem1Clicked() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CameraRequest);
            } else {
                Intent intent = new Intent(this, ScanActivity.class);
                startActivity(intent);
            }
        }

    }

    @Override
    public void onItem2Clicked() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GalleryRequest);
            } else {
                getImageFromGallery();
            }
        } else getImageFromGallery();
    }


    private void getImageFromGallery() {
        Intent innerIntent = new Intent();
        if (Build.VERSION.SDK_INT < 19) {
            innerIntent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            innerIntent.setAction(Intent.ACTION_PICK);
        }
        innerIntent.setType("image/*");
        Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");
        startActivityForResult(wrapperIntent, RequestCode);
    }
}
