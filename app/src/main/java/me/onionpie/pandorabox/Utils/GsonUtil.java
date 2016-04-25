package me.onionpie.pandorabox.Utils;

import com.google.gson.Gson;

/**
 * Created by wang on 2016/1/19.
 */
public class GsonUtil {

    private static GsonUtil sGsonUtil;

    private Gson mGson;

    public static synchronized GsonUtil getInstance(){
        if(sGsonUtil == null){
            sGsonUtil = new GsonUtil();
        }
        return sGsonUtil;
    }

    private GsonUtil(){
        mGson = new Gson();
    }

    public Gson getGson(){
        return mGson;
    }


}
