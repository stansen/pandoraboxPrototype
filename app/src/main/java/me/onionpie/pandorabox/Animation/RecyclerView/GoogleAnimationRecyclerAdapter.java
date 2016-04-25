package me.onionpie.pandorabox.Animation.RecyclerView;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * googleAdapter
 */
public abstract class GoogleAnimationRecyclerAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {

    protected int mAnimatedPosition = -1;

    public int getAnimatedPosition() {
        return mAnimatedPosition;
    }

    public void setAnimatedPosition(int animatedPosition) {
        this.mAnimatedPosition = animatedPosition;
    }

    public void reset() {
        mAnimatedPosition = -1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof GoogleAnimationViewHolder) {
            GoogleAnimationViewHolder vh = (GoogleAnimationViewHolder) holder;

            if (vh.animator.isRunning()) {
                vh.animator.end();
            }
            if (mAnimatedPosition < position) {
                vh.animator.start();
                mAnimatedPosition = position;
            }
        }
    }

    public static abstract class GoogleAnimationViewHolder extends RecyclerView.ViewHolder {

        AnimatorSet animator;

        public GoogleAnimationViewHolder(View itemView) {
            super(itemView);

            animator = new AnimatorSet();
            animator.playTogether(
                    ObjectAnimator.ofFloat(itemView, "translationY", 100, 0),
                    ObjectAnimator.ofFloat(itemView, "alpha", 0, 1.0f)
            );
            animator.setDuration(500);
        }
    }
}
