package me.onionpie.pandorabox.Utils;

import android.graphics.Color;

/**
 * Created by jiudeng009 on 2016/3/31.
 */
public class ColorUtil {
    public static boolean isLight(int color) {
        return Math.sqrt(Color.red(color) * Color.red(color) * .241 + Color.green(color) * Color.green(color) * .691 + Color.blue(color) * Color.blue(color) * .068) > 130;
    }

}
