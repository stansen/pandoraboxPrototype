package me.onionpie.pandorabox.UI.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.jiudeng007.barcodelib.QRHelper;
import com.example.jiudeng007.barcodelib.ScanActivity;
import com.example.jiudeng007.barcodelib.SheetDialog;
import com.example.jiudeng007.barcodelib.Utils;
import com.google.zxing.Result;

import me.onionpie.pandorabox.ConstansParam.Constans;
import me.onionpie.pandorabox.Model.PasswordTextInfoModel;
import me.onionpie.pandorabox.Utils.CommonPreference;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jiudeng007 on 2016/5/18.
 */
public class BaseCameraActivity extends BaseActivity implements SheetDialog.onDialogItemClickListener {

    private static final int OPENPASSWORDDETAIL = 10000;
    private static final int CameraRequest = 10002;
    private static final int GalleryRequest = 10003;
    public static final int RequestCode = 10001;
    /**
     * 1-HomeActivity 2-SynchronizationActivity 3-ExportActivity
     */
    public int mType ;
    public BaseInterface mBaseInterface;


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
                                        if (recode.equals(CommonPreference.getString(BaseCameraActivity.this, Constans.KEY_SCAN_CODE_VALUE))) {
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
                                        mBaseInterface.validateFailed();
                                    }

                                    @Override
                                    public void onNext(Boolean aBoolean) {
                                        switch (mType){
                                            case 1:
                                                if (aBoolean)
                                                    mBaseInterface.validateSuccess();
                                                else mBaseInterface.validateFailed();
                                                break;
                                            case 2:
                                                mBaseInterface.validateSuccess();
                                                break;
                                            case 3:
                                                if (aBoolean)
                                                    mBaseInterface.validateSuccess();
                                                else mBaseInterface.validateFailed();
                                                break;
                                        }
                                    }
                                });
                        addSubscription(subscription);
                    }
                    cursor.close();
            }
        }
    }

    public void showSheetDialog() {
        SheetDialog sheetDialog = new SheetDialog();
        sheetDialog.setOnDialogItemClickListener(this);
        sheetDialog.showDialog(this);
    }


    public void validatePasswordAndScanCode(PasswordTextInfoModel mPasswordTextInfoModel,int mPosition) {
        if (CommonPreference.getBoolean(getApplicationContext(), Constans.KEY_SET_SCAN_CODE_VALIDATE)
                && CommonPreference.getBoolean(getApplicationContext(), Constans.KEY_SET_SINGLE_PASSSWORD_VALIDATE)) {
            showPasswordDialog();
        } else if (CommonPreference.getBoolean(getApplicationContext(), Constans.KEY_SET_SCAN_CODE_VALIDATE)) {
            validateScanCode();
//            showSheetDialog();
        } else if (CommonPreference.getBoolean(getApplicationContext(), Constans.KEY_SET_SINGLE_PASSSWORD_VALIDATE)) {
            showPasswordDialog();
        } else {
            mBaseInterface.validateSuccess();
            Intent intent = PasswordDetailActivity.getStartIntent(this, false, mPasswordTextInfoModel, mPosition);
            startActivityForResult(intent, OPENPASSWORDDETAIL);
        }
    }
    public void validatePasswordAndScanCode(){
        if (CommonPreference.getBoolean(getApplicationContext(), Constans.KEY_SET_SCAN_CODE_VALIDATE)
                && CommonPreference.getBoolean(getApplicationContext(), Constans.KEY_SET_SINGLE_PASSSWORD_VALIDATE)) {
            showPasswordDialog();
        } else if (CommonPreference.getBoolean(getApplicationContext(), Constans.KEY_SET_SCAN_CODE_VALIDATE)) {
            validateScanCode();
//            showSheetDialog();
        } else if (CommonPreference.getBoolean(getApplicationContext(), Constans.KEY_SET_SINGLE_PASSSWORD_VALIDATE)) {
            showPasswordDialog();
        } else {
            mBaseInterface.validateSuccess();
//            switch (mType){
//                case 1:
//                    break;
//                case 2:
//                    break;
//                case 3:
//
//                    break;
//            }
        }
    }
    public void validateScanCode(){
        boolean isScanCodeUseful = CommonPreference.getBoolean(this, Constans.KEY_IS_SCAN_CODE_USEFUL);
        if (isScanCodeUseful) {
            mBaseInterface.validateSuccess();
//            Intent intent = PasswordDetailActivity.getStartIntent(this, false, mPasswordTextInfoModel, mPosition);
//            startActivityForResult(intent, OPENPASSWORDDETAIL);
        } else {
            showSheetDialog();
        }
    }
    public void showPasswordDialog() {
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
                                mBaseInterface.validateSuccess();
                            }
                        } else
                           mBaseInterface.validateFailed();
                    }
                }).show();
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
                    Toast.makeText(BaseCameraActivity.this, "拍照请求被拒绝了", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case GalleryRequest:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    getImageFromGallery();
                } else {
                    // Permission Denied
                    Toast.makeText(BaseCameraActivity.this, "相册请求被拒绝了", Toast.LENGTH_SHORT)
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


    public abstract class BaseInterface{
        abstract void validateSuccess();
        abstract void validateFailed();
    }
}
