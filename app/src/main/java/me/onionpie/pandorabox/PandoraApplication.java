package me.onionpie.pandorabox;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import me.onionpie.greendao.DBHelper;
import me.onionpie.pandorabox.ConstansParam.Constans;
import me.onionpie.pandorabox.Utils.CommonPreference;

/**
 * Created by Gstansen on 2016/4/12.
 */
public class PandoraApplication extends Application {
    private static Context mApplicationContext;
    private static PandoraApplication mInstance;
    public static byte[] scanCodeAk = new byte[16];
    public static String phone="";
    public static String toke="";
    public static Context getmApplicationContext() {
        return mApplicationContext;
    }
    public static PandoraApplication getmInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationContext = this;
        mInstance = this;
        scanCodeAk = getIV();
        DBHelper.init(this);
        String scanCode = CommonPreference.getString(this,Constans.KEY_SCAN_CODE_VALUE);
        if (TextUtils.isEmpty(scanCode)){
            CommonPreference.putBoolean(this,Constans.KEY_IS_SCAN_CODE_USEFUL,true);
        }
    }

    private byte[] getIV() {
        String iv = "1234567812345678"; //IV length: must be 16 bytes long
        return iv.getBytes();
    }
}
