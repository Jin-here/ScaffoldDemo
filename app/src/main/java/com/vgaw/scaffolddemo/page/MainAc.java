package com.vgaw.scaffolddemo.page;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.vgaw.scaffold.page.ScaffoldAc;
import com.vgaw.scaffold.page.ScaffoldFrag;
import com.vgaw.scaffold.util.dialog.DialogUtil;
import com.vgaw.scaffold.util.statusbar.StatusBarUtil;
import com.vgaw.scaffold.view.bottomnavigation.BottomNavigationLayout;
import com.vgaw.scaffold.view.checkable.OnItemCheckedListener;
import com.vgaw.scaffolddemo.R;
import com.vgaw.scaffolddemo.page.demo.compoment.CompomentFrag;
import com.vgaw.scaffolddemo.page.demo.example.ExampleFrag;
import com.vgaw.scaffolddemo.page.demo.internalpage.InternalPageFrag;

import java.util.List;

public class MainAc extends ScaffoldAc {
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

    private void initData() {
        mFragmentArray = new ScaffoldFrag[]{new CompomentFrag(), new InternalPageFrag(), new ExampleFrag()};
    }

    private void initView() {
        mMainBottomNavLayout.setOnItemCheckedListener(new OnItemCheckedListener() {
            @Override
            public void onItemChecked(int index) {
                showFragment(index);
            }
        });
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
