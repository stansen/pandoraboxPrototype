package com.example.jiudeng007.barcodelib;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.io.FileNotFoundException;
import java.util.Hashtable;

public class ScanCodeActivity extends AppCompatActivity implements SheetDialog.onDialogItemClickListener {
    private static final int CameraRequest = 10002;
    private static final int GalleryRequest = 10003;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);
        Button button = (Button) findViewById(R.id.choose);
        Button generateButton = (Button)findViewById(R.id.confirm);
        final EditText editText = (EditText)findViewById(R.id.scan_code_secret_key);
        final ImageView scancodeResult = (ImageView)findViewById(R.id.scan_code_image);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SheetDialog sheetDialog = new SheetDialog();
                sheetDialog.setOnDialogItemClickListener(ScanCodeActivity.this);
                sheetDialog.showDialog(ScanCodeActivity.this);
            }
        });
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Bitmap bitmap = QRHelper.createQRCode(editText.getText().toString(),350);
                    scancodeResult.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onItem1Clicked() {
        if(Build.VERSION.SDK_INT >=23){
            int checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (checkPermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CameraRequest);
            }
        }
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItem2Clicked() {
        if(Build.VERSION.SDK_INT >=23){
            int checkPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (checkPermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},GalleryRequest);
            }else {
              getImageFromGallery();
            }
        }else getImageFromGallery();

    }
    private void getImageFromGallery(){
        Intent innerIntent = new Intent();
        if (Build.VERSION.SDK_INT < 19) {
            innerIntent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            innerIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        innerIntent.setType("image/*");
        Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");
        startActivityForResult(wrapperIntent, RequestCode);
    }
    public static final int RequestCode = 10001;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case RequestCode:
                    String[] proj = { MediaStore.Images.Media.DATA };
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
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Result result = QRHelper.getScanBitmap(finalPhoto_path);
                                // String result = decode(photo_path);
                                if (result == null) {
                                    Looper.prepare();
                                    Toast.makeText(getApplicationContext(), "图片格式有误", Toast.LENGTH_SHORT)
                                            .show();
                                    Looper.loop();
                                } else {
                                    Log.i("123result", result.toString());
                                    // Log.i("123result", result.getText());
                                    // 数据返回
                                    String recode = QRHelper.recode(result.toString());
                                    Looper.prepare();
                                    Toast.makeText(getApplicationContext(), "解锁密码为："+recode, Toast.LENGTH_SHORT)
                                            .show();
                                    Looper.loop();
                                    Log.d("recode",recode);
                                }
                            }
                        }).start();
                    }

                    cursor.close();

                    break;

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CameraRequest:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
//                    getImageFromGallery();
                } else {
                    // Permission Denied
                    Toast.makeText(ScanCodeActivity.this, "拍照请求被拒绝了", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case GalleryRequest:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    getImageFromGallery();
                } else {
                    // Permission Denied
                    Toast.makeText(ScanCodeActivity.this, "相册请求被拒绝了", Toast.LENGTH_SHORT)
                            .show();
                }

                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
