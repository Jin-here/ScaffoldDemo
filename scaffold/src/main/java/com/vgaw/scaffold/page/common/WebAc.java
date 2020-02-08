package com.vgaw.scaffold.page.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.vgaw.scaffold.R;
import com.vgaw.scaffold.page.ScaffoldAc;
import com.vgaw.scaffold.util.statusbar.StatusBarUtil;

public class WebAc extends ScaffoldAc {
    private WebView mWebContent;

    public static void startActivity(Fragment fragment, String url) {
        Intent intent = new Intent(fragment.getContext(), WebAc.class);
        intent.putExtra("url", url);
        fragment.startActivity(intent);
    }

    public static void startActivity(Activity activity, String url) {
        Intent intent = new Intent(activity, WebAc.class);
        intent.putExtra("url", url);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_ac);
        StatusBarUtil.setColor(getSelf(), getResources().getColor(R.color.white));
        mWebContent = findViewById(R.id.web_content);

        findViewById(R.id.web_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mWebContent.setWebViewClient(new WebViewClient());
        mWebContent.requestFocusFromTouch();
        WebSettings webSettings = mWebContent.getSettings();
        webSettings.setJavaScriptEnabled(true);

        String url = getIntent().getStringExtra("url");
        mWebContent.loadUrl(url);

    }

    @Override
    public void onBackPressed() {
        if (mWebContent.canGoBack()) {
            mWebContent.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
