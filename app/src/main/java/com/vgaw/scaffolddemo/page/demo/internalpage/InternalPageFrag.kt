package com.vgaw.scaffolddemo.page.demo.internalpage

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tencent.bugly.beta.Beta
import com.vgaw.scaffold.page.MockFrag
import com.vgaw.scaffold.page.ReqCodeConstant
import com.vgaw.scaffold.page.common.ImgPreviewAc
import com.vgaw.scaffold.page.common.PermissionAc
import com.vgaw.scaffold.page.common.chooseimg.ChooseImgAc
import com.vgaw.scaffold.page.qrcode.ScanAc
import com.vgaw.scaffolddemo.R
import com.vgaw.scaffolddemo.page.feedback.FeedbackAc
import kotlinx.android.synthetic.main.internal_page_frag.view.*
import timber.log.Timber
import java.util.*

class InternalPageFrag : MockFrag() {
    companion object {
        private const val REQUEST_CODE_CHOOSE_IMG = 2
        private const val REQUEST_CODE_SCAN = 3
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.internal_page_frag, container, false)
        view.internalPageScan.setOnClickListener {ScanAc.startActivityForResult(getSelf(), REQUEST_CODE_SCAN)}
        view.internalPageChooseImg.setOnClickListener {ChooseImgAc.startActivityForResult(getSelf(), REQUEST_CODE_CHOOSE_IMG, null)}
        view.internalPageCheckVersion.setOnClickListener {Beta.checkUpgrade(true, false)}
        view.internalPageFeedback.setOnClickListener {FeedbackAc.startActivity(getSelf())}
        view.internalPagePermission.setOnClickListener {requestPermission()}
        view.internalPageImgPreview.setOnClickListener {ImgPreviewAc.startAc(getSelf(), buildImgList())}
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHOOSE_IMG) {
                Timber.d("file path: %s", data?.getStringExtra("data"))
            } else if (requestCode == REQUEST_CODE_SCAN) {
                Timber.d("qr msg: %s", data?.getStringExtra("qr_msg"))
            }
        }
    }

    private fun buildImgList(): ArrayList<String> {
        val imgList = ArrayList<String>()
        imgList.add("https://img.xsnvshen.com/album/19572/13044/000.jpg")
        imgList.add("https://img.xsnvshen.com/album/19572/13044/001.jpg")
        imgList.add("https://img.xsnvshen.com/album/19572/13044/003.jpg")
        imgList.add("https://img.xsnvshen.com/album/19572/13044/005.jpg")
        return imgList
    }

    private fun requestPermission() {
        val permissionArray = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA)
        mActivity?.let {
            if (!PermissionAc.Companion.hasPermission(it, permissionArray)) {
                PermissionAc.Companion.startActivityForResult(it, ReqCodeConstant.PERMISSION, permissionArray)
            }
        }
    }
}
