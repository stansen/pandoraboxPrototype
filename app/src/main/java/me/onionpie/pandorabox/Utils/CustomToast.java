package me.onionpie.pandorabox.Utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Gstansen on 2015/11/26.
 */
public class CustomToast {
    private volatile static CustomToast customedToast;
    private Toast mToast;
    private int iDuration = Toast.LENGTH_SHORT;
    private int iGravity = Gravity.CENTER_VERTICAL;

    public static CustomToast getCustomedToast() {
        if (customedToast == null) {
            synchronized (CustomToast.class) {
                if (customedToast == null) {
                    customedToast = new CustomToast();
                }
            }
        }
        return customedToast;
    }

    public void showToast(Context context, String content) {
        if (mToast == null) {
            mToast = Toast.makeText(context, content, iDuration);
        } else {
            mToast.setText(content);
        }
        mToast.setGravity(Gravity.BOTTOM, 0, Dip2PxUtil.Dip2Px(context, 64));
        mToast.show();
    }

    public void showCenterToast(Context context, String content) {
        if (mToast == null) {
            mToast = Toast.makeText(context, content, iDuration);
        } else {
            mToast.setText(content);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }
}
