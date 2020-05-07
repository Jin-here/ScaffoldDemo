package com.vgaw.scaffolddemo.page.demo.compoment.bottomnav;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;
import com.vgaw.scaffold.page.ScaffoldFrag;
import com.vgaw.scaffold.util.dialog.DialogUtil;
import com.vgaw.scaffold.util.statusbar.StatusBarUtil;
import com.vgaw.scaffold.view.TitleLayout;
import com.vgaw.scaffold.view.bottomnavigation.BottomNavigationLayout;
import com.vgaw.scaffold.view.checkable.OnItemCheckedListener;
import com.vgaw.scaffolddemo.R;
import com.vgaw.scaffolddemo.page.demo.compoment.Tab0Frag;
import com.vgaw.scaffolddemo.page.demo.compoment.Tab1Frag;
import com.vgaw.scaffolddemo.page.demo.compoment.Tab2Frag;

import java.util.List;

public class BottomNavFrag extends ScaffoldFrag {
    private BottomNavigationLayout mBottomNavBottomNavLayout;
    private TitleLayout mBottomNavTitleLayout;
    private MaterialButton mBottomNavShowBubble;
    private MaterialButton mBottomNavHideBubble;

    private ScaffoldFrag[] mFragmentArray;

    public static BottomNavFrag newInstance() {
        Bundle args = new Bundle();
        BottomNavFrag fragment = new BottomNavFrag();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_nav_frag, container, false);
        StatusBarUtil.addStatusbarHeight(getContext(), view);
        mBottomNavBottomNavLayout = view.findViewById(R.id.bottom_nav_bottom_nav_layout);
        mBottomNavTitleLayout = view.findViewById(R.id.bottom_nav_title_layout);
        mBottomNavShowBubble = view.findViewById(R.id.bottom_nav_show_bubble);
        mBottomNavHideBubble = view.findViewById(R.id.bottom_nav_hide_bubble);

        initView();
        return view;
    }

    private void initData() {
        mFragmentArray = new ScaffoldFrag[]{new Tab0Frag(), new Tab1Frag(), new Tab2Frag()};
    }

    private void initView() {
        mBottomNavBottomNavLayout.setOnItemCheckedListener(new OnItemCheckedListener() {
            @Override
            public void onItemChecked(int index) {
                showFragment(index);
            }
        });
        mBottomNavBottomNavLayout.check(0);

        mBottomNavTitleLayout.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.dismissDialog(getSelf());
            }
        });
        mBottomNavShowBubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomNavBottomNavLayout.setMsgTip(0, 100);
            }
        });
        mBottomNavHideBubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomNavBottomNavLayout.setMsgTip(0, 0);
            }
        });
    }

    private void showFragment(int index) {
        String tag = DialogUtil.buildTag(mFragmentArray[index]);

        FragmentManager fragmentManager = getChildFragmentManager();
        ScaffoldFrag fragment = (ScaffoldFrag) fragmentManager.findFragmentByTag(tag);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // hide other
        List<Fragment> fragmentList = fragmentManager.getFragments();
        if (fragmentList != null && fragmentList.size() > 0) {
            for (Fragment item : fragmentList) {
                if (item.isAdded()) {
                    fragmentTransaction.hide(item);
                }
            }
        }
        // showDialog index fragment
        if (fragment == null) {
            fragment = mFragmentArray[index];
            fragmentTransaction.add(R.id.bottom_nav_container, fragment, tag);
        } else {
            fragmentTransaction.show(fragment);
        }

        fragmentTransaction.commit();

        StatusBarUtil.setColor(getActivity(), getResources().getColor(fragment.getStatusBarColor()));
    }
}
