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
        DBHelper.init(this);
    }


}
