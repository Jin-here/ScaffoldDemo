package com.vgaw.scaffolddemo.page.feedback

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.vgaw.scaffold.page.ScaffoldAc
import com.vgaw.scaffold.util.statusbar.StatusBarUtil
import com.vgaw.scaffold.view.ScaffoldToast
import com.vgaw.scaffold.view.TitleLayout
import com.vgaw.scaffold.view.webview.FileChooserWebChromClient
import com.vgaw.scaffolddemo.R

class FeedbackAc : ScaffoldAc() {
    private lateinit var mFeedbackWv: WebView
    private lateinit var mFileChooserWebChromClient: FileChooserWebChromClient

    companion object {
        fun startActivity(activity: Activity) {
            activity.startActivity(Intent(activity, FeedbackAc::class.java))
        }

        fun startActivity(fragment: Fragment) {
            val intent = Intent(fragment.context, FeedbackAc::class.java)
            fragment.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feedback_ac)
        StatusBarUtil.setColor(this, Color.WHITE)

        findViewById<TitleLayout>(R.id.feedback_title_layout).setBackClickListener {onBackPressed()}
        mFeedbackWv = findViewById(R.id.feedback_wv)

        mFeedbackWv.settings.javaScriptEnabled = true
        mFeedbackWv.settings.domStorageEnabled = true
        mFeedbackWv.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null && url.startsWith("weixin://")) {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        view!!.context.startActivity(intent)
                    } catch (e: Exception) {
                        ScaffoldToast.show(R.string.feedback_wx_not_installed)
                    }
                    return true
                }
                return false
            }
        }
        mFileChooserWebChromClient = FileChooserWebChromClient(getSelf())
        mFeedbackWv.webChromeClient = mFileChooserWebChromClient
        val url = String.format("https://support.qq.com/product/%s", getString(R.string.txc_product_id))
        mFeedbackWv.loadUrl(url)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mFileChooserWebChromClient.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        if (mFeedbackWv.canGoBack()) {
            mFeedbackWv.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
