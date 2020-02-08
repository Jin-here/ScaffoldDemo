package com.vgaw.scaffold.view;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.vgaw.scaffold.R;

public class LoadingView extends AppCompatImageView {
    private ValueAnimator mAnimator;
    private boolean mAttached;
    private boolean mAnimEnabled = true;
    private float mStartRotation;

    private static final TimeInterpolator sInterpolator = new LinearInterpolator();

    public LoadingView(Context context) {
        super(context);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setAnimEnabled(boolean animEnabled){
        if (this.mAnimEnabled != animEnabled){
            this.mAnimEnabled = animEnabled;
            if (animEnabled && mAttached && getVisibility() == View.VISIBLE){
                startProgress();
            } else {
                stopProgress();
            }
        }
    }

    public boolean isAnimEnabled() {
        return mAnimEnabled;
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.VISIBLE){
            startProgress();
        } else {
            stopProgress();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAttached = true;
        startProgress();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAttached = false;
        stopProgress();
    }

    private void init() {
        setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.loading));
    }

    private void startProgress(){
        if (mAnimator != null){
            mAnimator.cancel();
            mAnimator = null;
        }

        if (!mAnimEnabled){
            return;
        }

        mAnimator = ValueAnimator.ofFloat(0, 1);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setDuration(1000);
        mAnimator.setInterpolator(sInterpolator);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = (float) animation.getAnimatedValue();
                updateRotation(percent);
            }
        });
        mAnimator.start();
    }

    private void stopProgress(){
        if (mAnimator != null){
            mAnimator.cancel();
            mAnimator = null;
        }

        updateRotation(0);
    }

    public void setStartRotation(float rotation){
        this.mStartRotation = rotation;
        updateRotation(0);
    }

    private void updateRotation(float percent){
        setPivotX(getMeasuredWidth() >> 1);
        setPivotY(getMeasuredHeight() >> 1);
        setRotation(360 * percent + mStartRotation);
    }
}
