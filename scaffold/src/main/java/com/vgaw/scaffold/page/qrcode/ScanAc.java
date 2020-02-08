package com.vgaw.scaffold.page.qrcode;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.vgaw.scaffold.R;
import com.vgaw.scaffold.page.ScaffoldAc;
import com.vgaw.scaffold.util.statusbar.StatusBarUtil;
import com.vgaw.scaffold.view.TitleLayout;

import timber.log.Timber;

public class ScanAc extends ScaffoldAc implements CameraPreview.Delegate {
    private static final int REQUEST_CODE_CAMERA_PERMISSION = 0x77;
    private FrameLayout mScanRoot;

    public static void startActivityForResult(Fragment fragment, int requestCode) {
        fragment.startActivityForResult(new Intent(fragment.getContext(), ScanAc.class), requestCode);
    }

    public static void startActivityForResult(Activity activity, int requestCode) {
        activity.startActivityForResult(new Intent(activity, ScanAc.class), requestCode);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_ac);
        StatusBarUtil.setColor(this, Color.TRANSPARENT);
        StatusBarUtil.setDarkMode(this);
        TitleLayout titleLayout = findViewById(R.id.scan_titlelayout);
        titleLayout.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        StatusBarUtil.addStatusbarHeight(this, titleLayout);

        mScanRoot = findViewById(R.id.scan_root);

        if (checkPermission()) {
            initQRCodeView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean granted = true;
        for (int item : grantResults) {
            if (item != PackageManager.PERMISSION_GRANTED) {
                granted = false;
                break;
            }
        }
        if (granted) {
            initQRCodeView();
        } else {
            finish();
        }
    }

    @Override
    public boolean onScanQRCodeSuccess(String result) {
        Timber.d("onScanQRCodeSuccess: %s", result);

        vibrate();

        Intent intent = getIntent();
        intent.putExtra("qr_msg", result);
        setResult(RESULT_OK, intent);
        finish();
        return true;
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Timber.d("onScanQRCodeOpenCameraError");
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    private void initQRCodeView() {
        QRCodeView qrCodeView = new QRCodeView(this);
        qrCodeView.setDelegate(this);

        mScanRoot.addView(qrCodeView, 0);
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA_PERMISSION);
            return false;
        }
        return true;
    }
}