package me.onionpie.pandorabox.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.Html;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;


/**
 * Created by jiudeng007 on 2016/4/11.
 */
public class UIHelpler  {
    /**
     * 发送App异常崩溃报告
     *
     * @param context
     */

    public static void sendAppCrashReport(final Context context) {
//        Intent intent = new Intent(context, WelcomeActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);

//        System.exit(-1);//关闭已奔溃的app进程
//        getConfirmDialog(context, "程序发生异常", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                // 退出
//                System.exit(-1);
////                android.os.Process.killProcess(android.os.Process.myPid());
//            }
//        }).show();
        new MaterialDialog.Builder(context).content("抱歉程序发生异常").positiveText("确定").onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(MaterialDialog dialog, DialogAction which) {
                // 退出

                System.exit(-1);
//                dialog.dismiss();
            }
        }).show();
//        DialogHelp.getConfirmDialog(context, "程序发生异常", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        }).show();
    }
    /***
     * 获取一个dialog
     * @param context
     * @return
     */
    public static AlertDialog.Builder getDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder;
    }
    public static AlertDialog.Builder getConfirmDialog(Context context, String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(Html.fromHtml(message));
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消", null);
        return builder;
    }
}
