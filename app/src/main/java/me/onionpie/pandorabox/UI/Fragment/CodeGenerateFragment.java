package me.onionpie.pandorabox.UI.Fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jiudeng007.barcodelib.QRHelper;
import com.google.zxing.WriterException;

import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.onionpie.pandorabox.ConstansParam.Constans;
import me.onionpie.pandorabox.PandoraApplication;
import me.onionpie.pandorabox.R;
import me.onionpie.pandorabox.Utils.CommonPreference;
import me.onionpie.pandorabox.Utils.Sercurity;

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
    private String scanCodeString;
    public CodeGenerateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String temp = CommonPreference.getString(getContext(), Constans.KEY_SCAN_CODE_VALUE);
        try {
            scanCodeString = Sercurity.aesDecrypt(temp,PandoraApplication.ak);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.v("scan_code",scanCodeString);
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
        return view;
    }

    private String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    @OnClick(R.id.confirm)
    public void onClickConfirm() {
        try {
            String uuid = generateUUID();
            Bitmap bitmap = QRHelper.createQRCode(uuid, 350);
            if (bitmap !=null){
                mCodeImage.setImageBitmap(bitmap);
                mConfirm.setVisibility(View.GONE);
                mCodeImage.setVisibility(View.VISIBLE);
                String destiny= Sercurity.aesEncrypt(scanCodeString, PandoraApplication.ak);
                CommonPreference.putString(getContext(),Constans.KEY_SCAN_CODE_VALUE,destiny);
            }

        } catch (WriterException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
