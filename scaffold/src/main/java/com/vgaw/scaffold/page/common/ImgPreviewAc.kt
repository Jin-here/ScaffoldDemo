package com.vgaw.scaffold.page.common

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.vgaw.scaffold.R
import com.vgaw.scaffold.page.ScaffoldAc
import com.vgaw.scaffold.util.statusbar.StatusBarUtil
import timber.log.Timber

class ImgPreviewAc : ScaffoldAc() {
    private lateinit var imgPreviewIndexHint: TextView

    private lateinit var mDataList: ArrayList<String>
    private var mCrtPosition = 0
    private lateinit var mAdapter: ImagePreviewAdapter
    
    companion object {
        fun startAc(fragment: Fragment, imgList: ArrayList<String>) = startAc(fragment, imgList, 0)

        /**
         * @param imgList 文件路径/图片url
         * @param selectIndex 默认显示第几张
         */
        fun startAc(fragment: Fragment, imgList: ArrayList<String>, selectIndex: Int) {
            if (imgList.size > 0) {
                val intent = Intent(fragment.getContext(), ImgPreviewAc::class.java)
                intent.putStringArrayListExtra("img_list", imgList)
                intent.putExtra("select_index", selectIndex)
                fragment.startActivity(intent)
                val activity = fragment.activity
                if (activity != null && !activity.isDestroyed) {
                    activity.overridePendingTransition(0, 0)
                }
            }
        }

        fun startAc(activity: Activity, imgList: ArrayList<String>) = startAc(activity, imgList, 0)

        fun startAc(activity: Activity, imgList: ArrayList<String>, selectIndex: Int) {
            if (imgList.size > 0) {
                val intent = Intent(activity, ImgPreviewAc::class.java)
                intent.putStringArrayListExtra("img_list", imgList)
                intent.putExtra("select_index", selectIndex)
                activity.startActivity(intent)
                activity.overridePendingTransition(0, 0)
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        makeDarkMode()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.img_preview_ac)
        val imgPreviewBack = findViewById<View>(R.id.img_preview_back)
        val imgPreviewBackLayout = findViewById<View>(R.id.img_preview_back_layout)
        val imgPreviewVp = findViewById<ViewPager>(R.id.img_preview_vp)
        imgPreviewIndexHint = findViewById(R.id.img_preview_index_hint)

        StatusBarUtil.addStatusbarHeight(this, imgPreviewBackLayout)
        StatusBarUtil.setColor(this, Color.TRANSPARENT)
        imgPreviewBack.setOnClickListener {finish()}

        getIntentData()

        mAdapter = ImagePreviewAdapter(this, mDataList)
        mAdapter.setListener { _, _, _ ->
            if (imgPreviewBackLayout.isShown) {
                imgPreviewBackLayout.visibility = View.GONE
            } else {
                imgPreviewBackLayout.visibility = View.VISIBLE
            }
        }
        imgPreviewVp.adapter = mAdapter
        imgPreviewVp.setCurrentItem(mCrtPosition, false)
        imgPreviewVp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                mCrtPosition = position
                Timber.d("on page selected")

                updateIndexHint()
            }
        })

        //初始化状态
        updateIndexHint()
    }

    private fun updateIndexHint() {
        imgPreviewIndexHint.text = String.format("%d/%d", mCrtPosition + 1, mDataList.size)
    }

    private fun getIntentData() {
        mCrtPosition = intent.getIntExtra("select_index", 0)
        mDataList = intent.getStringArrayListExtra("img_list")!!
        if (mCrtPosition < 0) {
            mCrtPosition = 0
        } else if (mCrtPosition > mDataList.size - 1) {
            mCrtPosition = mDataList.size - 1
        }
    }
}