package me.onionpie.pandorabox.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by jiudeng007 on 2016/3/23.
 */
public class RecyclableImageView extends ImageView {
    public RecyclableImageView(Context context) {
        super(context);
    }

    public RecyclableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setImageDrawable(null);
    }
}
