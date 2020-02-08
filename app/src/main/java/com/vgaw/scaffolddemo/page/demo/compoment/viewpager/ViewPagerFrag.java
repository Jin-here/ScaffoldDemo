package com.vgaw.scaffolddemo.page.demo.compoment.viewpager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vgaw.scaffold.page.ScaffoldFrag;
import com.vgaw.scaffold.util.dialog.DialogUtil;
import com.vgaw.scaffold.util.statusbar.StatusBarUtil;
import com.vgaw.scaffold.view.TitleLayout;
import com.vgaw.scaffolddemo.R;

public class ViewPagerFrag extends ScaffoldFrag {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_frag, container, false);
        StatusBarUtil.addStatusbarHeight(getContext(), view);
        TitleLayout mViewpagerTitleLayout = view.findViewById(R.id.viewpager_title_layout);
        mViewpagerTitleLayout.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.dismissDialog(getSelf());
            }
        });
        return view;
    }
}
