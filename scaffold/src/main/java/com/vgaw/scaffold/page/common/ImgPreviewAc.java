package com.vgaw.scaffold.page.common;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.vgaw.scaffold.R;
import com.vgaw.scaffold.page.ScaffoldAc;
import com.vgaw.scaffold.util.statusbar.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ImgPreviewAc extends ScaffoldAc {
    private TextView mImgPreviewIndexHint;
    private ViewPager mImgPreviewVp;
    private RelativeLayout mImgPreviewBackLayout;

    private List<String> mDataList;
    private int mCrtPosition = 0;
    private ImagePreviewAdapter mAdapter;

    public static void startAc(Fragment fragment, List<String> imgList) {
        startAc(fragment, imgList, 0);
    }

    public static void startAc(Fragment fragment, List<String> imgList, int selectIndex) {
        Intent intent = new Intent(fragment.getContext(), ImgPreviewAc.class);
        intent.putStringArrayListExtra("img_list", (ArrayList<String>) imgList);
        intent.putExtra("select_index", selectIndex);
        fragment.startActivity(intent);
        FragmentActivity activity = fragment.getActivity();
        if (activity != null && !activity.isDestroyed()) {
            activity.overridePendingTransition(0, 0);
        }
    }

    public static void startAc(Activity activity, List<String> imgList) {
        startAc(activity, imgList, 0);
    }

    public static void startAc(Activity activity, List<String> imgList, int selectIndex) {
        Intent intent = new Intent(activity, ImgPreviewAc.class);
        intent.putStringArrayListExtra("img_list", (ArrayList<String>) imgList);
        intent.putExtra("select_index", selectIndex);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDarkMode = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.img_preview_ac);
        mImgPreviewBackLayout = findViewById(R.id.img_preview_back_layout);
        StatusBarUtil.addStatusbarHeight(this, mImgPreviewBackLayout);
        StatusBarUtil.setColor(this, Color.TRANSPARENT);
        findViewById(R.id.img_preview_back).setOnClickListener(v -> finish());
        mImgPreviewIndexHint = findViewById(R.id.img_preview_index_hint);

        getIntentData();

        mImgPreviewVp = findViewById(R.id.img_preview_vp);
        mAdapter = new ImagePreviewAdapter(this, mDataList);
        mAdapter.setListener((view, v, v1) -> {
            if (mImgPreviewBackLayout.isShown()) {
                mImgPreviewBackLayout.setVisibility(View.GONE);
            } else {
                mImgPreviewBackLayout.setVisibility(View.VISIBLE);
            }
        });
        mImgPreviewVp.setAdapter(mAdapter);
        mImgPreviewVp.setCurrentItem(mCrtPosition, false);
        mImgPreviewVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                mCrtPosition = position;
                Timber.d("on page selected");

                updateIndexHint();
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        //初始化状态
        updateIndexHint();
    }

    private void updateIndexHint() {
        mImgPreviewIndexHint.setText(String.format("%d/%d", mCrtPosition + 1, mDataList.size()));
    }

    private void getIntentData() {
        mCrtPosition = getIntent().getIntExtra("select_index", 0);
        mDataList = getIntent().getStringArrayListExtra("img_list");
        if (mCrtPosition > mDataList.size() - 1 || mCrtPosition < 0) {
            mCrtPosition = 0;
        }
    }
}