package me.onionpie.jellybutton;

import android.view.animation.LinearInterpolator;

/**
 * @Author yb
 * @Date 2016/9/9
 */
public class JellyInterpolator extends LinearInterpolator {
    private float factor;

    public JellyInterpolator() {
        this.factor = 0.15f;
    }

    @Override
    public float getInterpolation(float input) {
//        Log.v("interpolator input", input + "");
        float result;
        result = (float) (Math.pow(2, -10 * input) * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
//        Log.v("interpolator result", result + "");
        return result;

    }
}
