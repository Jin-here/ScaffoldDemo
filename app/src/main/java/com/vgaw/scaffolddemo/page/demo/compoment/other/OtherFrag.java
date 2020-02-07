package com.vgaw.scaffolddemo.page.demo.compoment.other;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.vgaw.scaffold.page.BaseFrag;
import com.vgaw.scaffold.util.dialog.DialogUtil;
import com.vgaw.scaffold.util.statusbar.StatusBarUtil;
import com.vgaw.scaffold.view.CheckedImageButton;
import com.vgaw.scaffold.view.TitleLayout;
import com.vgaw.scaffolddemo.R;

public class OtherFrag extends BaseFrag {
    private NestedScrollView mOtherRoot;
    private CheckedImageButton mOtherCheckIb;
    private CheckedImageButton mOtherCheckIb1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.other_frag, container, false);
        StatusBarUtil.addStatusbarHeight(getContext(), view);
        mOtherRoot = view.findViewById(R.id.other_root);

        // toolbar
        TitleLayout otherTitleLayout = view.findViewById(R.id.other_title_layout);
        otherTitleLayout.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.dismissDialog(getSelf());
            }
        });

        // checkable imagebutton
        mOtherCheckIb = view.findViewById(R.id.other_checkedib);
        mOtherCheckIb.setNormalImageId(android.R.drawable.checkbox_off_background);
        mOtherCheckIb.setCheckedImageId(android.R.drawable.checkbox_on_background);
        mOtherCheckIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOtherCheckIb.setChecked(!mOtherCheckIb.isChecked());
            }
        });

        mOtherCheckIb1 = view.findViewById(R.id.other_checkedib1);
        mOtherCheckIb1.setNormalBkResId(android.R.drawable.checkbox_off_background);
        mOtherCheckIb1.setCheckedBkResId(android.R.drawable.checkbox_on_background);
        mOtherCheckIb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOtherCheckIb1.setChecked(!mOtherCheckIb1.isChecked());
            }
        });
        return view;
    }
}
