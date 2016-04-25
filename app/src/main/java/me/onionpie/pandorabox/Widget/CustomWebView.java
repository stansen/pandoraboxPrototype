package me.onionpie.pandorabox.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class CustomWebView extends WebView {
    float downY = 0;
    boolean needConsumeTouch = true; // 是否需要承包touch事件，needConsumeTouch一旦被定性，则不会更改

    public CustomWebView(Context arg0) {
        this(arg0, null);
    }

    public CustomWebView(Context arg0, AttributeSet arg1) {
        this(arg0, arg1, 0);
    }

    public CustomWebView(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    int lastX = 0;
    int lastY = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        if (ev.getPointerCount() > 1) {
            needConsumeTouch = true;
        } else if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downY = ev.getRawY();
            needConsumeTouch = true; // 默认情况下，listView内部的滚动优先，默认情况下由该listView去消费touch事件
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            int deltaX = x - lastX;
            int deltaY = y - lastY;
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            } else {
                // 在最顶端且向上拉了，则这个touch事件交给父类去处理
                getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            }
        }

        // 通知父view是否要处理touch事件
        getParent().requestDisallowInterceptTouchEvent(needConsumeTouch);
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 判断listView是否在顶部
     *
     * @return 是否在顶部
     */
    private boolean isAtTop() {
        return getScrollY() == 0;
    }

    private boolean isAtRight() {
        return getScrollX() == 0;
    }
}
