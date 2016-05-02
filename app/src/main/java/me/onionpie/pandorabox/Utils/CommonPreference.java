package me.onionpie.pandorabox.Utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by wang on 12/23/15.通用存储类
 */
public class CommonPreference {

    private static String NAME = "common_pref";


    public static void putString(Context context, String key, String value) {
        if (context == null) {
            return;
        }
        SharedPreferences pref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        pref.edit().putString(key, value).apply();
    }

    public static void putInt(Context context, String key, int value) {
        if (context == null) {
            return;
        }
        SharedPreferences pref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        pref.edit().putInt(key, value).apply();
    }

    public static void putBoolean(Context context, String key, boolean value) {
        if (context == null) {
            return;
        }

        SharedPreferences pref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        pref.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(Context context, String key){
        if (context == null) {
            return false;
        }

        SharedPreferences pref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(key, false);
    }

    public static String getString(Context context, String key) {
        if (context == null) {
            return null;
        }
        SharedPreferences pref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    public static int getInt(Context context, String key) {
        if (context == null) {
            return -1;
        }
        SharedPreferences pref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return pref.getInt(key, -1);
    }


    public static <T> Set<T> getSet (Context context, String key, Type myType) {
        String newSetValue = CommonPreference.getString(context, key);

        Set<T> newSet = null;
        if (newSetValue != null) {
            newSet = GsonUtil.getInstance().getGson().fromJson(newSetValue, myType);
        }

        if (newSet == null) {
            newSet = new HashSet<>();
        }
        return newSet;
    }

    public static <T> List<T> getList(Context context, String key, Type myType){
        String newListValue = CommonPreference.getString(context, key);
        List<T> newList = null;
        if (newListValue != null) {
            newList = GsonUtil.getInstance().getGson().fromJson(newListValue, myType);
        }
        return newList;
    }
    //    存储新通知id
    public static <T> void putSet(Context context, Set<T> value, String key) {
        putString(context, key, GsonUtil.getInstance().getGson().toJson(value));
    }

    public static <T> void putList(Context context, List<T> value, String key){
        putString(context, key, GsonUtil.getInstance().getGson().toJson(value));
    }

    public static void clear(Context context){
        SharedPreferences pref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        pref.edit().clear().apply();
    }

    public static void remove(Context context, String key){
        SharedPreferences pref = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        pref.edit().remove(key).apply();
    }
}
