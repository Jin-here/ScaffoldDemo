package com.vgaw.scaffold.page.qrcode;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.vgaw.scaffold.R;
import com.vgaw.scaffold.util.phone.DensityUtil;

public class ScanBoxView extends RelativeLayout {
    private ScanTorchLayout mScanScanTorchLayout;
    private View mScanMask;
    private View mScanLine;
    private ValueAnimator mAnimator;

    public ScanBoxView(Context context) {
        super(context);
        init();
    }

    public ScanBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScanBoxView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ScanBoxView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setOnTorchClickListener(OnClickListener listener) {
        mScanScanTorchLayout.setOnTorchClickListener(listener);
    }

    public int getRectWidth() {
        return DensityUtil.dp2px(getContext(), 224);
    }

    public void onRecognizeStart() {
        mAnimator = ValueAnimator.ofFloat(0, getRectWidth() - mScanLine.getHeight());
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();

                mScanLine.setY(mScanMask.getY() + animatedValue);
            }
        });
        mAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setDuration(2000);
        mAnimator.start();

        mScanLine.setVisibility(VISIBLE);
    }

    public void onRecognizeStop() {
        post(new Runnable() {
            @Override
            public void run() {
                if (mAnimator != null) {
                    mAnimator.cancel();
                    mAnimator = null;
                }
                mScanLine.setVisibility(GONE);
            }
        });
    }

    public void turnOnTorch() {
        mScanScanTorchLayout.turnOnTorch();
    }

    public void turnOffTorch() {
        mScanScanTorchLayout.turnOffTorch();
    }

    public void onCameraAmbientBrightnessChanged(boolean isDarkEnv, boolean torchOn) {
        mScanScanTorchLayout.onCameraAmbientBrightnessChanged(isDarkEnv, torchOn);
    }

    private void init() {
        View view = View.inflate(getContext(), R.layout.scan_layout, this);
        mScanScanTorchLayout = view.findViewById(R.id.scan_scantorchlayout);
        mScanMask = view.findViewById(R.id.scan_mask);
        mScanLine = view.findViewById(R.id.scan_line);
    }
}