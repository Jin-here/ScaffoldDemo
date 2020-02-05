package com.vgaw.scaffolddemo.page.demo.compoment.viewpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.vgaw.scaffold.img.ImgLoader;
import com.vgaw.scaffold.view.vp.EasyPagerAdapter;
import com.vgaw.scaffolddemo.R;

import java.util.List;

public class ADPagerAdapter extends EasyPagerAdapter<String> {
    public ADPagerAdapter(Context context, List<String> dataList) {
        super(context, dataList);
    }

    @Override
    protected View instantiateView(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.viewpager_ad_item, null);
        ImageView iv = view.findViewById(R.id.viewpager_ad_item_content);
        String item = getItem(position);
        ImgLoader.load(mContext, item, iv);
        return view;
    }
}