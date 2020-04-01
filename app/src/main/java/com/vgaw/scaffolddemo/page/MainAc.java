package com.vgaw.scaffolddemo.page;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.vgaw.scaffold.page.ScaffoldAc;
import com.vgaw.scaffold.page.ScaffoldFrag;
import com.vgaw.scaffold.util.dialog.DialogUtil;
import com.vgaw.scaffold.util.statusbar.StatusBarUtil;
import com.vgaw.scaffold.view.AppToast;
import com.vgaw.scaffold.view.bottomnavigation.BottomNavigationLayout;
import com.vgaw.scaffolddemo.R;
import com.vgaw.scaffolddemo.page.demo.compoment.CompomentFrag;
import com.vgaw.scaffolddemo.page.demo.example.ExampleFrag;
import com.vgaw.scaffolddemo.page.demo.internalpage.InternalPageFrag;

import java.util.List;

public class MainAc extends ScaffoldAc {
    private static final long BACK_TO_EXIT_DURATION = 1400;
    private long mLstBackTime;

    private BottomNavigationLayout mMainBottomNavLayout;

    private ScaffoldFrag[] mFragmentArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_ac);
        mMainBottomNavLayout = findViewById(R.id.main_bottom_nav_layout);

        initData();
        initView();
    }

    @Override
    public void onBackPressed() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount > 0) {
            super.onBackPressed();
            return;
        }

        long crtTime = System.currentTimeMillis();
        if (crtTime - mLstBackTime > BACK_TO_EXIT_DURATION) {
            mLstBackTime = crtTime;
            AppToast.show(R.string.back_again_to_exit);
        } else {
            super.onBackPressed();
        }
    }

    private void initData() {
        mFragmentArray = new ScaffoldFrag[]{new CompomentFrag(), new InternalPageFrag(), new ExampleFrag()};
    }

    private void initView() {
        mMainBottomNavLayout.setOnItemCheckedListener(this::showFragment);
        mMainBottomNavLayout.check(0);
    }

    private void showFragment(int index) {
        String tag = DialogUtil.buildTag(mFragmentArray[index]);

        FragmentManager fragmentManager = getSupportFragmentManager();
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
            fragmentTransaction.add(R.id.main_container, fragment, tag);
        } else {
            fragmentTransaction.show(fragment);
        }

        fragmentTransaction.commit();

        StatusBarUtil.setColor(this, getResources().getColor(fragment.getStatusBarColor()));
    }
}
