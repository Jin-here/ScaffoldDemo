package com.vgaw.scaffold.page.common

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.vgaw.scaffold.R
import com.vgaw.scaffold.page.ScaffoldAc
import com.vgaw.scaffold.util.statusbar.StatusBarUtil

class WebAc : ScaffoldAc() {
    private lateinit var mWebContent: WebView

    companion object {
        fun startActivity(fragment: Fragment, url: String) {
            val intent = Intent(fragment.context, WebAc::class.java)
            intent.putExtra("url", url)
            fragment.startActivity(intent)
        }

        fun startActivity(activity: Activity, url: String) = startActivity(activity, url, false)

        fun startActivity(fragment: Fragment, url: String, noCache: Boolean) {
            val intent = Intent(fragment.context, WebAc::class.java)
            intent.putExtra("url", url)
            intent.putExtra("no_cache", noCache)
            fragment.startActivity(intent)
        }

        fun startActivity(activity: Activity, url: String, noCache: Boolean) {
            val intent = Intent(activity, WebAc::class.java)
            intent.putExtra("url", url)
            intent.putExtra("no_cache", noCache)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_ac)
        StatusBarUtil.setColor(getSelf(), getResources().getColor(R.color.white))
        findViewById<View>(R.id.web_back).setOnClickListener {onBackPressed()}
        mWebContent = findViewById(R.id.web_content)

        val noCache = intent.getBooleanExtra("no_cache", false)
        if (noCache) {
            mWebContent.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        }
        mWebContent.webViewClient = WebViewClient()
        mWebContent.requestFocusFromTouch()
        val webSettings = mWebContent.settings
        webSettings.javaScriptEnabled = true

        val url = intent.getStringExtra("url")!!
        mWebContent.loadUrl(url)
    }

    override fun onBackPressed() {
        if (mWebContent.canGoBack()) {
            mWebContent.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
