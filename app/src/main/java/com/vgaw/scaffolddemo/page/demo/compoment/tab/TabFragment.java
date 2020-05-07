package com.vgaw.scaffolddemo.page.demo.compoment.tab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.vgaw.scaffold.page.ScaffoldFrag;
import com.vgaw.scaffold.util.dialog.DialogUtil;
import com.vgaw.scaffold.util.statusbar.StatusBarUtil;
import com.vgaw.scaffold.view.TitleLayout;
import com.vgaw.scaffold.view.tab.DefaultTabAdapter;
import com.vgaw.scaffold.view.tab.DefaultTabStyleBuilder;
import com.vgaw.scaffold.view.tab.SlidingTabLayout;
import com.vgaw.scaffold.view.tab.indicator.RoundRectShortIndicator;
import com.vgaw.scaffold.view.vp.EasyFragmentPagerAdapter;
import com.vgaw.scaffolddemo.R;
import com.vgaw.scaffolddemo.page.demo.compoment.Tab0Frag;
import com.vgaw.scaffolddemo.page.demo.compoment.Tab1Frag;
import com.vgaw.scaffolddemo.page.demo.compoment.Tab2Frag;

public class TabFragment extends ScaffoldFrag {
    private TitleLayout mTabTitleLayout;
    private SlidingTabLayout mTabTab;
    private ViewPager mTabVp;
    private View mTabShowBubble;
    private View mTabHideBubble;

    private ScaffoldFrag[] mFragArray;
    private String[] mFragTitleArray;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_frag, container, false);
        StatusBarUtil.addStatusbarHeight(getContext(), view);
        mTabTitleLayout = view.findViewById(R.id.tab_title_layout);
        mTabTab = view.findViewById(R.id.tab_tab);
        mTabVp = view.findViewById(R.id.tab_vp);
        mTabShowBubble = view.findViewById(R.id.tab_show_bubble);
        mTabHideBubble = view.findViewById(R.id.tab_hide_bubble);

        initData();
        initView();
        return view;
    }

    private void initData() {
        mFragArray = new ScaffoldFrag[3];
        mFragArray[0] = new Tab0Frag();
        mFragArray[1] = new Tab1Frag();
        mFragArray[2] = new Tab2Frag();

        mFragTitleArray = new String[]{"tab0", "tab1", "tab2"};
    }

    private void initView() {
        mTabVp.setAdapter(new EasyFragmentPagerAdapter(getChildFragmentManager(), mFragArray));
        mTabTab.setAdapter(new DefaultTabAdapter(getActivity(), mFragTitleArray, new DefaultTabStyleBuilder()
                .txtColor(R.color.black, R.color.black2)
                .txtSize(16, 22).build()));
        mTabTab.setSelectedIndicator(new RoundRectShortIndicator(getContext()));
        mTabTab.setViewPager(mTabVp, 0);

        mTabTitleLayout.setBackClickListener(v -> DialogUtil.dismissDialog(getSelf()));
        mTabShowBubble.setOnClickListener(v -> mTabTab.setMsgTip(0, 100));
        mTabHideBubble.setOnClickListener(v -> mTabTab.setMsgTip(0, 0));
    }
}
