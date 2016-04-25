package me.onionpie.pandorabox.Widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by jiudeng007 on 2016/3/17.
 */
public class CustViewPager extends ViewPager {
    public CustViewPager(Context context) {
        super(context);
    }

    public CustViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int mOldX = 0;
    private int mOldY = 0;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mOldX;
                int deltaY = y - mOldY;
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    intercept = true;
                } else
                    intercept = false;
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
        }
        mOldY = y;
        mOldX = x;
        return intercept;
    }
}
