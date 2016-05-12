package me.onionpie.pandorabox.UI.Fragment;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiudeng007.barcodelib.QRHelper;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.onionpie.pandorabox.ConstansParam.Constans;
import me.onionpie.pandorabox.PandoraApplication;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.Utils.CommonPreference;
import me.onionpie.pandorabox.Utils.FileUtil;
import me.onionpie.pandorabox.Utils.Sercurity;
import me.onionpie.pandorabox.glide.ImageLoadManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class CodeGenerateFragment extends Fragment {


    @Bind(R.id.confirm)
    Button mConfirm;
    @Bind(R.id.title)
    TextView mTitle;
    @Bind(R.id.code_image)
    ImageView mCodeImage;
    private String scanCodeString = "";
//    @Bind(R.id.test)
//    ImageView mImageView;

    public CodeGenerateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scanCodeString = CommonPreference.getString(getContext(), Constans.KEY_SCAN_CODE_VALUE);
        Log.v("scan_code", scanCodeString);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_code_generate, container, false);
        ButterKnife.bind(this, view);
        if (TextUtils.isEmpty(scanCodeString)) {
            mConfirm.setVisibility(View.VISIBLE);
            mTitle.setVisibility(View.GONE);
        } else {
            mConfirm.setVisibility(View.GONE);
            mTitle.setVisibility(View.VISIBLE);
        }
//        getImageView();
        return view;
    }

    private String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    @OnClick(R.id.confirm)
    public void onClickConfirm() {
//        clickConfirm();
        getPermission();
    }

    private void clickConfirm() {
        try {
            String uuid = generateUUID();
            scanCodeString = uuid;
            Bitmap bitmap = QRHelper.createQRCode(uuid, 350);
            if (bitmap != null) {
                mCodeImage.setImageBitmap(bitmap);
                mConfirm.setVisibility(View.GONE);
                mCodeImage.setVisibility(View.VISIBLE);
                saveImage(bitmap);
                media();
                CommonPreference.putString(getContext(), Constans.KEY_SCAN_CODE_VALUE, scanCodeString);
//                String destiny = Sercurity.aesEncrypt(scanCodeString, PandoraApplication.scanCodeAk);
////                Log.v("value",Sercurity.aesDecrypt(destiny,PandoraApplication.scanCodeAk));
//                CommonPreference.putString(getContext(), Constans.KEY_SCAN_CODE_VALUE, destiny);
            }

        } catch (WriterException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void media(){
        MediaScannerConnection.scanFile(getActivity(), new String[]{FileUtil.getSDCardPath() + File.separator + "pandoraBox"+ File.separator + "code.png"}, null, null);
//        MediaScannerConnection mediaScannerConnection=new MediaScannerConnection(ESearchManager.this, client);
////获取连接
//        mediaScannerConnection.connect()
    }
    private void getPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
            } else {
                clickConfirm();
            }
        } else clickConfirm();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    clickConfirm();
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "相册请求被拒绝了", Toast.LENGTH_SHORT)
                            .show();
                }
                break;

        }
    }

    private void saveImage(Bitmap bitmap) {
//        FileUtil.
//        File folder = FileUtil.getSaveFolder("pandoraBox");
        File temp = new File(FileUtil.getSDCardPath() + File.separator + "pandoraBox");
//        File temp = get().getDir("pandora", Context.MODE_PRIVATE);
        if (!temp.exists()) {
            if (temp.mkdir()) {
                Log.v("pandora_file", "success");
            }
        }
//        if (!folder.exists())
//            folder.mkdir();
        File file = new File(temp.getAbsolutePath() + File.separator + "code.png");

        try {
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.i("bitmap_saved", "已经保存");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
//    MediaScannerConnection.MediaScannerConnectionClient client=new MediaScannerConnection.MediaScannerConnectionClient() {
//
//        public void onScanCompleted(String path, Uri uri) {
//            // TODO Auto-generated method stub
//            mediaScannerConnection.disconnect();
//            Log.d("tag", "onScanCompleted");
//        }
//
//        public void onMediaScannerConnected() {
//            // TODO Auto-generated method stub
//            Log.d("tag", "onMediaScannerConnected");
//        }
//    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
