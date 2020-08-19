package com.vgaw.scaffold.view.special;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.vgaw.scaffold.R;

public class WaveView extends View {
    private static final float DEFAULT_DIVIDER = 0.4F;
    private static final long INTERVAL = 28;
    private int mMinRadius;
    private int mMaxRadius;
    private int mWaveColor;
    private int mDuration = 2000;
    private float mWaveDivider;

    private float mMaxGoingRadius = -1;
    private Paint mPaint;

    private boolean mStarted;

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void stopWave() {
        mStarted = false;
    }

    public boolean startWave() {
        if (!mStarted) {
            mStarted = true;

            resetParam();

            invalidate();
            return true;
        }
        return false;
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == INVISIBLE || visibility == GONE) {
            // View不见后，onDraw会停止执行，因此需重置
            stopWave();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopWave();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mStarted) {
            return;
        }

        for (float goingRadius = mMaxGoingRadius;;goingRadius = nextGoingRadius(goingRadius)) {
            float realRaw = getRealRaw(goingRadius);
            if (realRaw > mMinRadius) {
                drawOne(canvas, goingRadius);
            } else {
                break;
            }
        }

        addGoingRadius();

        postInvalidateDelayed(INTERVAL);
    }

    private float nextGoingRadius(float raw) {
        float dividerLength = (float) (mMaxRadius - mMinRadius) * mWaveDivider;
        return (raw - dividerLength);
    }

    private float getRealRaw(float raw) {
        return ((raw - mMinRadius) % (mMaxRadius - mMinRadius)) + mMinRadius;
    }

    private void drawOne(Canvas canvas, float goingRadius) {
        float radius = addRadius(goingRadius);
        int color = addColor(goingRadius);

        mPaint.setColor(color);
        float strokeWidth = (radius - mMinRadius);
        mPaint.setStrokeWidth(strokeWidth);
        canvas.drawCircle(mMaxRadius, mMaxRadius, mMinRadius + strokeWidth / 2, mPaint);
    }

    private void init(AttributeSet attrs) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.WaveView);
        mMinRadius = array.getDimensionPixelSize(R.styleable.WaveView_waveMinRadius, 120);
        mMaxRadius = array.getDimensionPixelOffset(R.styleable.WaveView_waveMaxRadius, 160);
        mWaveDivider = array.getFloat(R.styleable.WaveView_waveDivider, DEFAULT_DIVIDER);
        mWaveColor = array.getColor(R.styleable.WaveView_waveColor, getResources().getColor(R.color.colorAccent));

        array.recycle();
    }

    private void resetParam() {
        mMaxGoingRadius = mMinRadius;
    }

    private void addGoingRadius() {
        mMaxGoingRadius += ((float) (mMaxRadius - mMinRadius) / (mDuration / INTERVAL));
    }

    private float addRadius(float goingRadius) {
        return goingRadius;
    }

    private int addColor(float goingRadius) {
        int a = (int) (((goingRadius - mMinRadius) / (mMaxRadius - mMinRadius)) * 255.0);
        if (a > 255) {
            a = 255;
        }
        return Color.argb(255 - a,
                Color.red(mWaveColor), Color.green(mWaveColor), Color.blue(mWaveColor));
    }
}
