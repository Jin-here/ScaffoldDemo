package com.vgaw.scaffolddemo.page.demo.internalpage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vgaw.scaffold.page.MockFrag;
import com.vgaw.scaffold.page.common.ChooseImgAc;
import com.vgaw.scaffold.page.qrcode.ScanAc;
import com.vgaw.scaffolddemo.R;
import com.vgaw.scaffolddemo.page.feedback.FeedbackAc;

import timber.log.Timber;

public class InternalPageFrag extends MockFrag {
    private static final int REQUEST_CODE_CHOOSE_IMG = 2;
    private static final int REQUEST_CODE_SCAN = 3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.internal_page_frag, container, false);
        view.findViewById(R.id.internal_page_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanAc.startActivityForResult(getSelf(), REQUEST_CODE_SCAN);
            }
        });
        view.findViewById(R.id.internal_page_choose_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImgAc.startActivityForResult(getSelf(), REQUEST_CODE_CHOOSE_IMG);
            }
        });
        view.findViewById(R.id.internal_page_check_version).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // bugly业务调整，暂时无法使用
            }
        });
        view.findViewById(R.id.internal_page_feedback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedbackAc.startActivity(getSelf());
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHOOSE_IMG) {
                Timber.d("file path: %s", data.getStringExtra("file_path"));
            } else if (requestCode == REQUEST_CODE_SCAN) {
                Timber.d("qr msg: %s", data.getStringExtra("qr_msg"));
            }
        }
    }
}
