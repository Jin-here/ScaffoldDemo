package com.vgaw.scaffolddemo.page.demo.compoment.recyclerview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vgaw.scaffold.page.BaseFrag;
import com.vgaw.scaffold.util.dialog.DialogUtil;
import com.vgaw.scaffold.util.statusbar.StatusBarUtil;
import com.vgaw.scaffold.view.TitleLayout;
import com.vgaw.scaffolddemo.R;

public class NormalRecyclerViewFrag extends BaseFrag {
    private TitleLayout mNormalRecyclerviewTitleLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.normal_recyclerview_frag, container, false);
        StatusBarUtil.addStatusbarHeight(getContext(), view);
        mNormalRecyclerviewTitleLayout = view.findViewById(R.id.normal_recyclerview_title_layout);

        mNormalRecyclerviewTitleLayout.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.dismissDialog(getSelf());
            }
        });
        return view;
    }
}
