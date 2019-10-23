package com.vgaw.scaffold.page.qrcode;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class QRCodeView extends FrameLayout {
    /**
     * response for camera and recognize
     */
    private CameraPreview mCameraPreview;
    /**
     * response for ui
     */
    private ScanBoxView mScanBoxView;

    public QRCodeView(@NonNull Context context) {
        super(context);
        init();
    }

    public QRCodeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QRCodeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public QRCodeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /**
     * 设置扫描二维码的代理
     *
     * @param delegate 扫描二维码的代理
     */
    public void setDelegate(CameraPreview.Delegate delegate) {
        mCameraPreview.setDelegate(delegate);
    }

    protected void onCameraAmbientBrightnessChanged(boolean isDarkEnv, boolean torchOn) {
        mScanBoxView.onCameraAmbientBrightnessChanged(isDarkEnv, torchOn);
    }

    private void init() {
        mScanBoxView = new ScanBoxView(getContext());
        mScanBoxView.setOnTorchClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean suc = mCameraPreview.switchTorch();
                if (suc) {
                    if (mCameraPreview.torchOn()) {
                        mScanBoxView.turnOnTorch();
                    } else {
                        mScanBoxView.turnOffTorch();
                    }
                }
            }
        });

        mCameraPreview = new CameraPreview(getContext());
        mCameraPreview.setOnRecognizeStateListener(new CameraPreview.RecognizeStateListener() {
            @Override
            public void onRecognizeStart() {
                mScanBoxView.onRecognizeStart();
            }

            @Override
            public void onRecognizeStop() {
                mScanBoxView.onRecognizeStop();
            }
        });
        mCameraPreview.setData(this, mScanBoxView.getRectWidth());

        addView(mCameraPreview, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER));
        addView(mScanBoxView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER));
    }
}