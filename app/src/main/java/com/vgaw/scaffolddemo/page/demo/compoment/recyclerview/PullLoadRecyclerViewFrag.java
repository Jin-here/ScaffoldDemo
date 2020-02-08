package com.vgaw.scaffolddemo.page.demo.compoment.recyclerview;

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

public class PullLoadRecyclerViewFrag extends ScaffoldFrag {
    private TitleLayout mPullLoadRecyclerviewTitleLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pull_load_recyclerview_frag, container, false);
        StatusBarUtil.addStatusbarHeight(getContext(), view);
        mPullLoadRecyclerviewTitleLayout = view.findViewById(R.id.pull_load_recyclerview_title_layout);

        mPullLoadRecyclerviewTitleLayout.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.dismissDialog(getSelf());
            }
        });
        return view;
    }
}
