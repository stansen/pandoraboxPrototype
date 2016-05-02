package me.onionpie.pandorabox.UI.View;

import android.content.Context;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import me.onionpie.pandorabox.R;

/**
 * Created by Gstansen on 2016/5/2.
 */
public class PasswordDetailRuleView {
    public  void showDialog(int position, Context context){
        switch (position){
            //hide
            case 0:
              showHideDialog(context);
                break;
            //replace
            case 1:
                break;
            //reverse
            case 2:
                break;
            default:
                break;
        }
    }
    private void showHideDialog(Context context){
     MaterialDialog dialog =  new MaterialDialog.Builder(context)
                .title("隐藏")
                .customView(R.layout.hide_dialog_view,true)
                .show();
        View view = dialog.getCustomView();

    }
}
