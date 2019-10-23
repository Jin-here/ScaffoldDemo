package com.vgaw.scaffolddemo.page.feedback;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vgaw.scaffold.page.BaseAc;
import com.vgaw.scaffold.util.statusbar.StatusBarUtil;
import com.vgaw.scaffold.view.TitleLayout;
import com.vgaw.scaffold.view.webview.FileChooserWebChromClient;
import com.vgaw.scaffolddemo.R;

public class FeedbackAc extends BaseAc {
    private WebView mFeedbackWv;
    private FileChooserWebChromClient mFileChooserWebChromClient;

    public static void startActivity(Activity activity) {
        activity.startActivity(new Intent(activity, FeedbackAc.class));
    }

    public static void startActivity(Fragment fragment) {
        Intent intent = new Intent(fragment.getContext(), FeedbackAc.class);
        fragment.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_ac1);
        StatusBarUtil.setColor(this, Color.WHITE);
        mFeedbackWv = findViewById(R.id.feedback_wv);
        TitleLayout feedbackTitleyout = findViewById(R.id.feedback_titlelayout);

        feedbackTitleyout.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mFeedbackWv.getSettings().setJavaScriptEnabled(true);
        mFeedbackWv.getSettings().setDomStorageEnabled(true);
        mFeedbackWv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        mFileChooserWebChromClient = new FileChooserWebChromClient(getSelf());
        mFeedbackWv.setWebChromeClient(mFileChooserWebChromClient);
        String url = String.format("https://support.qq.com/product/%s", getString(R.string.tucao_product_id));
        mFeedbackWv.loadUrl(url);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFileChooserWebChromClient.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (mFeedbackWv.canGoBack()) {
            mFeedbackWv.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
