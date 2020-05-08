package com.vgaw.scaffolddemo.page.feedback

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.vgaw.scaffold.page.ScaffoldAc
import com.vgaw.scaffold.util.statusbar.StatusBarUtil
import com.vgaw.scaffold.view.webview.FileChooserWebChromClient
import com.vgaw.scaffolddemo.R
import kotlinx.android.synthetic.main.feedback_ac.*

class FeedbackAc : ScaffoldAc() {
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

        feedbackTitleLayout.setBackClickListener {onBackPressed()}

        feedbackWv.settings.javaScriptEnabled = true
        feedbackWv.settings.domStorageEnabled = true
        feedbackWv.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return super.shouldOverrideUrlLoading(view, url)
            }
        }
        mFileChooserWebChromClient = FileChooserWebChromClient(getSelf())
        feedbackWv.webChromeClient = mFileChooserWebChromClient
        val url = String.format("https://support.qq.com/product/%s", getString(R.string.txc_product_id))
        feedbackWv.loadUrl(url)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mFileChooserWebChromClient.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        if (feedbackWv.canGoBack()) {
            feedbackWv.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
