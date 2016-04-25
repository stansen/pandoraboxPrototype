package me.onionpie.pandorabox.Utils;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;

/**
 * Created by jiudeng007 on 2015/12/31.
 */
public class TextColorSizeUtil {

    /**
     * 获取资源中的颜色
     *
     * @param color
     * @return
     */
    public static int getResourcesColor(int color, Context context) {

        int ret = 0x00ffffff;
        try {
            ret = context.getResources().getColor(color);
        } catch (Exception e) {
        }
        return ret;
    }


    public static Spanned getTextFromHtml(String source, int color,Context context) {

        return Html.fromHtml("<font color=#" + getResourcesColor(color,context) + ">" + source + "</font>");
    }
}
