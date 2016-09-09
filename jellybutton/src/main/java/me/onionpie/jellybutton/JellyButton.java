package me.onionpie.jellybutton;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * @Author yb
 * @Date 2016/9/9
 */
public class JellyButton extends Button {
    public JellyButton(Context context) {
        super(context);
        init();
    }

    public JellyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public JellyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public JellyButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    private void init(){
        mOnButtonClickListener = new onButtonClickListener();
        setOnClickListener(mOnButtonClickListener);
    }
    private onButtonClickListener mOnButtonClickListener;

    @Override
    public void setOnClickListener(OnClickListener l) {
        if (l instanceof onButtonClickListener) {
            super.setOnClickListener(l);
        } else {
            if (mOnButtonClickListener != null)
                mOnButtonClickListener.setListener(l);
        }

    }

    public class onButtonClickListener implements OnClickListener {
        OnClickListener mOnClickListener;

        public void setListener(OnClickListener clickListener) {
            this.mOnClickListener = clickListener;
        }

        @Override
        public void onClick(View view) {
            showJellyAnimation(view);
            if (mOnClickListener != null) {
                mOnClickListener.onClick(view);
            }
        }
    }

    private void showJellyAnimation(final View view){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view,"scale",0.95f,1.0f).setDuration(1200);
        objectAnimator.setInterpolator(new JellyInterpolator());
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float val = (Float) valueAnimator.getAnimatedValue();
                view.setScaleX(val);
                view.setScaleY(val);
            }
        });
        objectAnimator.start();
    }
}
