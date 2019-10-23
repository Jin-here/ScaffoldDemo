package com.vgaw.scaffold.page.qrcode;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.vgaw.scaffold.R;

public class ScanTorchLayout extends LinearLayout {
    private ImageView mScanTorchIv;
    private TextView mScanTorchTv;

    private Boolean mShown;

    public ScanTorchLayout(Context context) {
        super(context);
        init();
    }

    public ScanTorchLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScanTorchLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ScanTorchLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setOnTorchClickListener(OnClickListener listener) {
        setOnClickListener(listener);
    }

    public void turnOnTorch() {
        mScanTorchIv.setImageResource(R.drawable.torch_on);
        mScanTorchTv.setText(R.string.turn_off_torch);
    }

    public void turnOffTorch() {
        mScanTorchIv.setImageResource(R.drawable.torch_off);
        mScanTorchTv.setText(R.string.turn_on_torch);
    }

    public void onCameraAmbientBrightnessChanged(boolean isDarkEnv, boolean torchOn) {
        // 亮度低 || 闪光灯已打开，则show
        boolean show = isDarkEnv || torchOn;
        onShowChanged(show);
    }

    private void init() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        View view = View.inflate(getContext(), R.layout.scan_torch_layout, this);
        mScanTorchIv = view.findViewById(R.id.scan_torch_iv);
        mScanTorchTv = view.findViewById(R.id.scan_torch_tv);

        onShowChanged(false);

        turnOffTorch();
    }

    private void onShowChanged(boolean nowShown) {
        if (mShown == null || mShown != nowShown) {
            mShown = nowShown;
            setVisibility(mShown ? VISIBLE : GONE);
        }
    }
}
