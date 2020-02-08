package com.vgaw.scaffold.page.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.github.chrisbanes.photoview.PhotoView;
import com.vgaw.scaffold.R;
import com.vgaw.scaffold.img.ImgLoadListener;
import com.vgaw.scaffold.img.ImgLoader;
import com.vgaw.scaffold.view.LoadingView;
import com.vgaw.scaffold.view.vp.EasyPagerAdapter;

import java.util.List;

public class ImagePreviewAdapter extends EasyPagerAdapter<String> {
    public PhotoViewClickListener mListener;

    public ImagePreviewAdapter(Context context, List<String> dataList) {
        super(context, dataList);
    }

    public void setListener(PhotoViewClickListener listener) {
        this.mListener = listener;
    }

    @Override
    protected View instantiateView(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.img_preview_item, null);
        PhotoView imgPreviewItemContent = view.findViewById(R.id.img_preview_item_content);
        LoadingView imgPreviewItemLoading = view.findViewById(R.id.img_preview_item_loading);
        imgPreviewItemContent.setOnPhotoTapListener((view1, x, y) -> {
            if (mListener != null) {
                mListener.OnPhotoTapListener(view1, x, y);
            }
        });

        String imgUrl = getItem(position);
        ImgLoader.load(mContext, imgUrl, imgPreviewItemContent, new ImgLoadListener() {
            @Override
            public void onLoadSuccess() {
                imgPreviewItemLoading.setVisibility(View.GONE);
            }

            @Override
            public void onLoadFailed() {}
        });
        return view;
    }

    public interface PhotoViewClickListener {
        void OnPhotoTapListener(View view, float v, float v1);
    }
}
