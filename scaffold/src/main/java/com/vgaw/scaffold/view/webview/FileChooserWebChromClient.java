package com.vgaw.scaffold.view.webview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.vgaw.scaffold.util.FileUtil;

import java.io.File;

/**
 * onActivityResult() should be invoked.
 */
public class FileChooserWebChromClient extends WebChromeClient {
    private static final int FILE_CHOOSE_REQUEST_CODE = 90;
    private ValueCallback<Uri[]> mUploadMessage;
    private ValueCallback<Uri> mUploadMessage1;
    private Activity mActivity;

    public FileChooserWebChromClient(Activity activity) {
        mActivity = activity;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == FILE_CHOOSE_REQUEST_CODE) {
            if (null == mUploadMessage) {
                return;
            }
            Uri result = data == null || resultCode != Activity.RESULT_OK ? null : data.getData();
            if (result == null) {
                if (mUploadMessage != null) {
                    mUploadMessage.onReceiveValue(null);
                    mUploadMessage = null;
                } else if (mUploadMessage1 != null) {
                    mUploadMessage1.onReceiveValue(null);
                    mUploadMessage1 = null;
                }
                return;
            }
            String path = FileUtil.getPath(mActivity, result);
            if (TextUtils.isEmpty(path)) {
                if (mUploadMessage != null) {
                    mUploadMessage.onReceiveValue(null);
                    mUploadMessage = null;
                } else if (mUploadMessage1 != null) {
                    mUploadMessage1.onReceiveValue(null);
                    mUploadMessage1 = null;
                }
                return;
            }
            Uri uri = Uri.fromFile(new File(path));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mUploadMessage.onReceiveValue(new Uri[]{uri});
                mUploadMessage = null;
            } else {
                mUploadMessage1.onReceiveValue(uri);
                mUploadMessage1 = null;
            }
        }
    }

    // should be in proguard whitelist
    // For Android 4.1
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(null);
        }
        mUploadMessage1 = uploadMsg;

        Intent intent = new Intent();
        intent.setAction("android.intent.action.PICK");
        intent.addCategory("android.intent.category.DEFAULT");
        String type = TextUtils.isEmpty(acceptType) ? "image/*" : acceptType;
        intent.setType(type);
        mActivity.startActivityForResult(intent, FILE_CHOOSE_REQUEST_CODE);
    }

    //Android 5.0+
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(null);
        }
        mUploadMessage = filePathCallback;

        Intent intent = new Intent();
        intent.setAction("android.intent.action.PICK");
        intent.addCategory("android.intent.category.DEFAULT");

        if (fileChooserParams != null && fileChooserParams.getAcceptTypes() != null
                && fileChooserParams.getAcceptTypes().length > 0) {
            intent.setType(fileChooserParams.getAcceptTypes()[0]);
        } else {
            intent.setType("image/*");
        }

        mActivity.startActivityForResult(intent, FILE_CHOOSE_REQUEST_CODE);
        return true;
    }
}
