package com.vgaw.scaffolddemo.page.demo.internalpage;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tencent.bugly.beta.Beta;
import com.vgaw.scaffold.page.MockFrag;
import com.vgaw.scaffold.page.ReqCodeConstant;
import com.vgaw.scaffold.page.common.ChooseImgAc;
import com.vgaw.scaffold.page.common.ImgPreviewAc;
import com.vgaw.scaffold.page.common.PermissionAc;
import com.vgaw.scaffold.page.qrcode.ScanAc;
import com.vgaw.scaffolddemo.R;
import com.vgaw.scaffolddemo.page.feedback.FeedbackAc;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class InternalPageFrag extends MockFrag {
    private static final int REQUEST_CODE_CHOOSE_IMG = 2;
    private static final int REQUEST_CODE_SCAN = 3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.internal_page_frag, container, false);
        view.findViewById(R.id.internal_page_scan).setOnClickListener(v -> ScanAc.startActivityForResult(getSelf(), REQUEST_CODE_SCAN));
        view.findViewById(R.id.internal_page_choose_img).setOnClickListener(v -> ChooseImgAc.Companion.startActivityForResult(getSelf(), REQUEST_CODE_CHOOSE_IMG, null));
        view.findViewById(R.id.internal_page_check_version).setOnClickListener(v -> {
            Beta.checkUpgrade(true, false);
        });
        view.findViewById(R.id.internal_page_feedback).setOnClickListener(v -> FeedbackAc.startActivity(getSelf()));
        view.findViewById(R.id.internal_page_permission).setOnClickListener(v -> requestPermission());
        view.findViewById(R.id.internal_page_img_preview).setOnClickListener(v -> ImgPreviewAc.startAc(getSelf(), buildImgList()));
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHOOSE_IMG) {
                Timber.d("file path: %s", data.getStringExtra("data"));
            } else if (requestCode == REQUEST_CODE_SCAN) {
                Timber.d("qr msg: %s", data.getStringExtra("qr_msg"));
            }
        }
    }

    private List<String> buildImgList() {
        List<String> imgList = new ArrayList<>();
        imgList.add("https://img.xsnvshen.com/album/19572/13044/000.jpg");
        imgList.add("https://img.xsnvshen.com/album/19572/13044/001.jpg");
        imgList.add("https://img.xsnvshen.com/album/19572/13044/003.jpg");
        imgList.add("https://img.xsnvshen.com/album/19572/13044/005.jpg");
        return imgList;
    }

    private void requestPermission() {
        String[] permissionArray = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA};
        if (!PermissionAc.hasPermission(mActivity, permissionArray)) {
            PermissionAc.startActivityForResult(mActivity, ReqCodeConstant.PERMISSION, permissionArray);
        }
    }
}
