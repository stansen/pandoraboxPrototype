package me.onionpie.pandorabox.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebView;

public class CustWebView extends WebView {
    boolean allowDragTop = true; // 如果是true，则允许拖动至底部的下一页
    float downY = 0;
    boolean needConsumeTouch = true; // 是否需要承包touch事件，needConsumeTouch一旦被定性，则不会更改

    public CustWebView(Context arg0) {
        this(arg0, null);
    }

    public CustWebView(Context arg0, AttributeSet arg1) {
        this(arg0, arg1, 0);
    }

    public CustWebView(Context arg0, AttributeSet arg1, int arg2) {
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
            needConsumeTouch = true; // 默认情况下，webview内部的滚动优先，默认情况下由该webview去消费touch事件
            allowDragTop = isAtTop();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            int deltaX = x - lastX;
            int deltaY = y - lastY;
//            if ()
            //当滑动角度大于30度则认为是竖向否则为横向

            if (deltaY == 0 && Math.abs(deltaX) > 0) {
                getParent().requestDisallowInterceptTouchEvent(false);
                Log.d("custwebview", "x" + deltaX + "y" + deltaY + "parent deal with it");
                return false;
            }
            if (deltaY != 0 && Math.abs(deltaX / deltaY) > 1.73) {
                getParent().requestDisallowInterceptTouchEvent(false);
                Log.d("custwebview", "x" + deltaX + "y" + deltaY + "parent deal with it");
                return false;
            } else if (!needConsumeTouch) {
                // 在最顶端且向上拉了，则这个touch事件交给父类去处理
                getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            } else if (allowDragTop) {
                // needConsumeTouch尚未被定性，此处给其定性
                // 允许拖动到底部的下一页，而且又向上拖动了，就将touch事件交给父view
                if (ev.getRawY() - downY > 2) {
                    // flag设置，由父类去消费
                    needConsumeTouch = false;
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                }
            }
        }
        lastX = x;
        lastY = y;
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
