package com.vgaw.scaffold.view.vp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class EasyFragmentPagerAdapter extends FragmentPagerAdapter {
    private Fragment[] mFragArray;

    public EasyFragmentPagerAdapter(@NonNull FragmentManager fm, @NonNull Fragment[] fragArray) {
        // TODO: 2020/3/18 mode
        super(fm);
        mFragArray = fragArray;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragArray[position];
    }

    @Override
    public int getCount() {
        return mFragArray.length;
    }
}
