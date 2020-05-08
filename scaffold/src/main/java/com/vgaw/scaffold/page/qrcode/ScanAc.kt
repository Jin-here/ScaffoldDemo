package com.vgaw.scaffold.page.qrcode

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Vibrator
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.vgaw.scaffold.R
import com.vgaw.scaffold.page.ScaffoldAc
import com.vgaw.scaffold.util.statusbar.StatusBarUtil
import kotlinx.android.synthetic.main.scan_ac.*
import timber.log.Timber

class ScanAc : ScaffoldAc(), CameraPreview.Delegate {
    companion object {
        private const val REQUEST_CODE_CAMERA_PERMISSION = 0x77

        fun startActivityForResult(fragment: Fragment, requestCode: Int) {
            fragment.startActivityForResult(Intent(fragment.context, ScanAc::class.java), requestCode)
        }

        fun startActivityForResult(activity: Activity, requestCode: Int) {
            activity.startActivityForResult(Intent(activity, ScanAc::class.java), requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scan_ac)
        StatusBarUtil.setColor(this, Color.TRANSPARENT)
        StatusBarUtil.setDarkMode(this)
        scanTitleLayout.setBackClickListener {finish()}
        StatusBarUtil.addStatusbarHeight(this, scanTitleLayout)

        if (checkPermission()) {
            initQRCodeView()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var granted = true
        for (item in grantResults) {
            if (item != PackageManager.PERMISSION_GRANTED) {
                granted = false
                break
            }
        }
        if (granted) {
            initQRCodeView()
        } else {
            finish()
        }
    }

    override fun onScanQRCodeSuccess(result: String?): Boolean {
        Timber.d("onScanQRCodeSuccess: %s", result)

        vibrate()

        val intent = intent
        intent.putExtra("qr_msg", result)
        setResult(RESULT_OK, intent)
        finish()
        return true
    }

    override fun onScanQRCodeOpenCameraError() {
        Timber.d("onScanQRCodeOpenCameraError")
    }

    private fun vibrate() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(200)
    }

    private fun initQRCodeView() {
        val qrCodeView = QRCodeView(this)
        qrCodeView.setDelegate(this)

        scanRoot.addView(qrCodeView, 0)
    }

    private fun checkPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA), REQUEST_CODE_CAMERA_PERMISSION)
            return false
        }
        return true
    }
}