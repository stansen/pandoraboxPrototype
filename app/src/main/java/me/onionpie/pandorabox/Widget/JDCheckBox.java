package me.onionpie.pandorabox.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;

/**
 * Created by jiudeng007 on 2015/12/18.
 */
public class JDCheckBox extends ImageView implements Checkable {

    private boolean mIsChecked;
    private static final int[] CHECKED_STATE_SET = { android.R.attr.state_checked };
    public JDCheckBox(Context context) {
        super(context);
    }

    public JDCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked())
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        return super.onCreateDrawableState(extraSpace);
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked != mIsChecked){
            mIsChecked = checked;
            refreshDrawableState();
        }
    }
    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }
    @Override
    public boolean isChecked() {
        return mIsChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mIsChecked);
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }
}
