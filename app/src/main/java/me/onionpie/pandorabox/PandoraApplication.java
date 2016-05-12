package me.onionpie.pandorabox;

import android.app.Application;
import android.content.Context;

import me.onionpie.greendao.DBHelper;

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
    }

    private byte[] getIV() {
        String iv = "1234567812345678"; //IV length: must be 16 bytes long
        return iv.getBytes();
    }
}
