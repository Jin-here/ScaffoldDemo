package com.vgaw.scaffold.view.webview

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.vgaw.scaffold.util.FileUtil
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.launch
import java.io.File

/**
 * onActivityResult() should be invoked.
 */
class FileChooserWebChromClient(val activity: AppCompatActivity): WebChromeClient() {
    companion object {
        private val FILE_CHOOSE_REQUEST_CODE = 90
        private val MAX_IMG_UPLOAD_SIZE: Long = 6 * 1024 * 1024
    }

    private var mUploadMessage: ValueCallback<Array<Uri>>? = null
    private var mUploadMessage1: ValueCallback<Uri>? = null
    private val mActivity: AppCompatActivity

    init {
        mActivity = activity
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FILE_CHOOSE_REQUEST_CODE) {
            if (null == mUploadMessage) {
                return
            }
            var result: Uri? = null
            if ((data != null || resultCode == Activity.RESULT_OK)) {result = data?.data}
            if (result == null) {
                if (mUploadMessage != null) {
                    mUploadMessage!!.onReceiveValue(null)
                    mUploadMessage = null
                } else if (mUploadMessage1 != null) {
                    mUploadMessage1!!.onReceiveValue(null)
                    mUploadMessage1 = null
                }
                return
            }
            val tempFile = FileUtil.from(mActivity, result)
            // compress
            val fileSize = tempFile.length()
            if (fileSize > MAX_IMG_UPLOAD_SIZE) {
                mActivity.lifecycleScope.launch {
                    val compressedFile = Compressor.compress(mActivity, tempFile) {
                        size(MAX_IMG_UPLOAD_SIZE)
                    }
                    val resultPath = compressedFile.absolutePath
                    onGetResult1(resultPath)
                }
            } else{
                onGetResult1(tempFile.absolutePath)
            }
        }
    }

    private fun onGetResult1(path: String?) {
        if (TextUtils.isEmpty(path)) {
            if (mUploadMessage != null) {
                mUploadMessage!!.onReceiveValue(null)
                mUploadMessage = null
            } else if (mUploadMessage1 != null) {
                mUploadMessage1!!.onReceiveValue(null)
                mUploadMessage1 = null
            }
            return
        }
        val uri = Uri.fromFile(File(path))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mUploadMessage!!.onReceiveValue(arrayOf(uri))
            mUploadMessage = null
        } else {
            mUploadMessage1!!.onReceiveValue(uri)
            mUploadMessage1 = null
        }
    }

    // should be in proguard whitelist
    // For Android 4.1
    fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String, capture: String) {
        if (mUploadMessage != null) {
            mUploadMessage!!.onReceiveValue(null)
        }
        mUploadMessage1 = uploadMsg

        val intent = Intent()
        intent.setAction("android.intent.action.PICK")
        intent.addCategory("android.intent.category.DEFAULT")
        val type:String = if (TextUtils.isEmpty(acceptType)) "image/*" else acceptType
        intent.setType(type)
        mActivity.startActivityForResult(intent, FILE_CHOOSE_REQUEST_CODE)
    }

    //Android 5.0+
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onShowFileChooser(webView: WebView, filePathCallback: ValueCallback<Array<Uri>>, fileChooserParams: FileChooserParams?): Boolean {
        if (mUploadMessage != null) {
            mUploadMessage!!.onReceiveValue(null)
        }
        mUploadMessage = filePathCallback

        val intent = Intent()
        intent.setAction("android.intent.action.PICK")
        intent.addCategory("android.intent.category.DEFAULT")

        if (fileChooserParams != null && fileChooserParams.getAcceptTypes() != null
                && fileChooserParams.getAcceptTypes().size > 0) {
            intent.setType(fileChooserParams.getAcceptTypes()[0])
        } else {
            intent.setType("image/*")
        }

        mActivity.startActivityForResult(intent, FILE_CHOOSE_REQUEST_CODE)
        return true
    }
}
