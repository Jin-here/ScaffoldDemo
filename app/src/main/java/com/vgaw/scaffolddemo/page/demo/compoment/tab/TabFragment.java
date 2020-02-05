package com.vgaw.scaffolddemo.page.demo.compoment.tab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.vgaw.scaffold.page.BaseFrag;
import com.vgaw.scaffold.util.dialog.DialogUtil;
import com.vgaw.scaffold.util.statusbar.StatusBarUtil;
import com.vgaw.scaffold.view.TitleLayout;
import com.vgaw.scaffold.view.tab.SlidingTabLayout;
import com.vgaw.scaffold.view.tab.indicator.RoundRectShortIndicator;
import com.vgaw.scaffolddemo.R;
import com.vgaw.scaffolddemo.page.demo.compoment.Tab0Frag;
import com.vgaw.scaffolddemo.page.demo.compoment.Tab1Frag;
import com.vgaw.scaffolddemo.page.demo.compoment.Tab2Frag;

public class TabFragment extends BaseFrag {
    private TitleLayout mTabTitleLayout;
    private SlidingTabLayout mTabTab;
    private ViewPager mTabVp;

    private BaseFrag[] mFragArray;
    private String[] mFragTitleArray;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_frag, container, false);
        StatusBarUtil.addStatusbarHeight(getContext(), view);
        mTabTitleLayout = view.findViewById(R.id.tab_title_layout);
        mTabTab = view.findViewById(R.id.tab_tab);
        mTabVp = view.findViewById(R.id.tab_vp);

        initData();
        initView();
        return view;
    }

    private void initData() {
        mFragArray = new BaseFrag[3];
        mFragArray[0] = new Tab0Frag();
        mFragArray[1] = new Tab1Frag();
        mFragArray[2] = new Tab2Frag();

        mFragTitleArray = new String[]{"tab0", "tab1", "tab2"};
    }

    private void initView() {
        mTabVp.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return mFragArray[position];
            }

            @Override
            public int getCount() {
                return mFragArray.length;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mFragTitleArray[position];
            }
        });
        mTabTab.setCustomTabView(R.layout.tab_item, null);
        mTabTab.setSelectedIndicator(new RoundRectShortIndicator(getContext(), mFragArray.length));
        mTabTab.setViewPager(mTabVp, 0);

        mTabTitleLayout.setBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.dismissDialog(getSelf());
            }
        });
    }
}
