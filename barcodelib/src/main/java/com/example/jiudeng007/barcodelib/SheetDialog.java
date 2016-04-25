package com.example.jiudeng007.barcodelib;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by jiudeng007 on 2016/4/20.
 */
public class SheetDialog implements View.OnClickListener{

    private BottomSheetDialog mBottomSheetDialog;

    public void showDialog(Context context) {
        mBottomSheetDialog = new BottomSheetDialog(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null);

        mBottomSheetDialog.setContentView(dialogView);

        TextView mItem1 = (TextView) dialogView.findViewById(R.id.item1);

        TextView mItem2 = (TextView) dialogView.findViewById(R.id.item2);

        TextView mItem3 = (TextView) dialogView.findViewById(R.id.item3);
        mItem1.setOnClickListener(this);
        mItem2.setOnClickListener(this);
        mItem3.setOnClickListener(this);
        mBottomSheetDialog.show();

    }


    private onDialogItemClickListener mOnDialogItemClickListener;

    public void setOnDialogItemClickListener(onDialogItemClickListener onDialogItemClickListener) {
        mOnDialogItemClickListener = onDialogItemClickListener;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.item1) {
            if (mOnDialogItemClickListener != null)
                mOnDialogItemClickListener.onItem1Clicked();
            mBottomSheetDialog.dismiss();

        } else if (i == R.id.item2) {
            if (mOnDialogItemClickListener != null)
                mOnDialogItemClickListener.onItem2Clicked();
            mBottomSheetDialog.dismiss();

        } else if (i == R.id.item3) {
            mBottomSheetDialog.dismiss();

        }
    }

    public interface onDialogItemClickListener {
        void onItem1Clicked();

        void onItem2Clicked();
    }
}
